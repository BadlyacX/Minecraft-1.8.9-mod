package com.badlyac.forgemod.Aim;

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

public class AutoAim {

    public static KeyBinding toggleAutoPvP;
    private boolean isEnabled = false;
    private Entity targetedEntity = null;
    private KeyBinding keyForward;
    public double nearestDistance = 0;

    static {
        toggleAutoPvP = new KeyBinding("Toggle AutoAim Bot", Keyboard.KEY_NONE, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(toggleAutoPvP);
    }


    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (toggleAutoPvP.isPressed()) {
            isEnabled = !isEnabled;
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText((isEnabled ? (EnumChatFormatting.GREEN + "AutoAim enabled") : (EnumChatFormatting.RED + "AutoAim disabled"))));
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
            if (targetedEntity != null && (player.getDistanceToEntity(targetedEntity) > nearestDistance || !targetedEntity.isEntityAlive())) {
                targetedEntity = null; // Stop tracking dead entities or entities out of range
            }
            if (targetedEntity == null) {
                targetedEntity = getNearestEntity(player); // Find a new entity to target
            }
            if (targetedEntity != null) {
                if (!(targetedEntity instanceof EntityItem)) {
                    aimAtEntity(targetedEntity);
                }
            }
        }
    }

    private Entity getNearestEntity(EntityPlayer player) {
        AxisAlignedBB searchBox = player.getEntityBoundingBox().expand(nearestDistance, nearestDistance, nearestDistance);
        List<Entity> list = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, searchBox);
        Entity nearestEntity = null;
        if (player.isSpectator()) {
            nearestDistance = 0;
        } else if (player.capabilities.isCreativeMode) {
            nearestDistance = 5.0;
        } else {
            nearestDistance = 3.5;
        }

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
        if (entity != null) {
            double deltaX = entity.posX - mc.thePlayer.posX;
            double deltaY = entity.posY + (double)(entity.getEyeHeight()) - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
            double deltaZ = entity.posZ - mc.thePlayer.posZ;
            double dist = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
            float yaw = (float)(Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI) - 90.0F;
            float pitch = (float)(-(Math.atan2(deltaY, dist) * 180.0D / Math.PI));
            mc.thePlayer.rotationYaw = yaw;
            mc.thePlayer.rotationPitch = pitch;
        }
    }
}