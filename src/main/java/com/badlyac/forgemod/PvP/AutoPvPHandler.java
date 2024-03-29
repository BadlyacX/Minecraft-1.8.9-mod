package com.badlyac.forgemod.PvP;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Random;

public class AutoPvPHandler {

    public static KeyBinding toggleAutoPvP;
    private boolean isEnabled = false;
    private Entity targetedEntity = null;
    private static KeyBinding keyForward;
    private static final double MAX_DISTANCE = 20.0;

    static {
        toggleAutoPvP = new KeyBinding("Toggle PvP Bot", Keyboard.KEY_NONE, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(toggleAutoPvP);
    }

    public AutoPvPHandler() {
        keyForward = Minecraft.getMinecraft().gameSettings.keyBindForward;
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (toggleAutoPvP.isPressed()) {
            isEnabled = !isEnabled;
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText((isEnabled ? (EnumChatFormatting.GREEN + "AutoPvP enabled") : (EnumChatFormatting.RED + "AutoPvP disabled"))));
            if (!isEnabled) {
                targetedEntity = null;
                keyForward.unPressAllKeys();
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (isEnabled && player != null) {
            if (targetedEntity != null && (!targetedEntity.isEntityAlive() || player.getDistanceToEntity(targetedEntity) > MAX_DISTANCE)) {
                targetedEntity = null;
            }
            if (targetedEntity == null) {
                targetedEntity = getNearestEntity(player);
            }
            if (targetedEntity != null && !(targetedEntity instanceof EntityItem)) {
                aimAtEntity(targetedEntity);
                autoAttack();
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
            }
        }
    }

    private Entity getNearestEntity(EntityPlayer player) {
        AxisAlignedBB searchBox = player.getEntityBoundingBox().expand(MAX_DISTANCE, MAX_DISTANCE, MAX_DISTANCE);
        List<Entity> list = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, searchBox);
        Entity nearestEntity = null;
        double nearestDistance = MAX_DISTANCE;

        for (Entity entity : list) {
            double distance = player.getDistanceToEntity(entity);
            if (distance < nearestDistance) {
                nearestEntity = entity;
                nearestDistance = distance;
            }
        }

        return nearestEntity;
    }

    private void aimAtEntity(Entity entity) {
        Minecraft mc = Minecraft.getMinecraft();
        double p = 0.3;
        double p1 = 0.3;
        if (entity != null) {
            double deltaX = entity.posX - mc.thePlayer.posX;
            Random random = new Random();
            int a = random.nextInt(100);
            if (a >= 75) {
                int r = random.nextInt(100);
                if (r >= 75) {
                    p = p * random.nextDouble() * 3;
                    p = p * -1;
                }
            } else if (a <= 15) {
                int r = random.nextInt(100);
                if (r >= 75) {
                    p1 = p1 *-1;
                }
            }
            double deltaY = entity.posY + (double) (entity.getEyeHeight()) - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight() + p);
            double deltaZ = (entity.posZ - mc.thePlayer.posZ) + p1;
            double dist = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

            float yaw = (float) (Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI) - 90.0F;
            float pitch = (float) (-(Math.atan2(deltaY, dist) * 180.0D / Math.PI));
            mc.thePlayer.rotationYaw = yaw;
            mc.thePlayer.rotationPitch = pitch;
        }
    }


    private void autoAttack() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (player == null) return;

        double playerAttackRange;
        if (player.capabilities.isCreativeMode) {
            playerAttackRange = 5.0;
        } else if (player.isSpectator()) {
            playerAttackRange = 0;
        } else {
            playerAttackRange = 3.2;
        }

        Entity nearestEntity = getNearestEntityWithinRange(player, playerAttackRange);

        if (nearestEntity != null) {
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
            KeyBinding.onTick(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode());
        }
    }

    private Entity getNearestEntityWithinRange(EntityPlayer player, double range) {
        AxisAlignedBB searchBox = player.getEntityBoundingBox().expand(range, range, range);
        List<Entity> list = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, searchBox);
        Entity nearestEntity = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Entity entity : list) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityPlayer) && player.canEntityBeSeen(entity)) {
                double distance = player.getDistanceToEntity(entity);
                if (distance < nearestDistance && distance <= range) {
                    nearestEntity = entity;
                    nearestDistance = distance;
                }
            }
        }
        return nearestEntity;
    }
}