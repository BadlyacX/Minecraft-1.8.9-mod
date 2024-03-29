package com.badlyac.forgemod.AFK;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class AFKBot {

    private final Minecraft mc = Minecraft.getMinecraft();
    private static final KeyBinding toggleKey;
    private boolean isAntiAFKActive = false;
    private boolean isWalkable = true;
    private long lastHeadMoveTime = 0;
    private long lastForwardPressTime = 0;

    static {
        toggleKey = new KeyBinding("Toggle AFK Bot", Keyboard.KEY_NONE, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(toggleKey);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onTick(TickEvent.ClientTickEvent event) {

        if (event.phase == TickEvent.Phase.END) {
            if (toggleKey.isPressed()) {
                isAntiAFKActive = !isAntiAFKActive;
                mc.thePlayer.addChatMessage(new ChatComponentText(isAntiAFKActive ? EnumChatFormatting.GREEN + "AFK Bot enabled" : EnumChatFormatting.RED + "AFK Bot disabled"));

            }

            if (isAntiAFKActive) {
                isWalkable = false;
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastHeadMoveTime >= 70) {
                    mc.thePlayer.rotationYaw += 15.0F;
                    lastHeadMoveTime = currentTime;
                }

                if (currentTime - lastForwardPressTime >= 500) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                    KeyBinding.onTick(mc.gameSettings.keyBindForward.getKeyCode());
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                    lastForwardPressTime = currentTime;
                }
            }
            if (!isAntiAFKActive && !isWalkable) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.onTick(mc.gameSettings.keyBindForward.getKeyCode());
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                isWalkable = true;
            }
        }
    }
}