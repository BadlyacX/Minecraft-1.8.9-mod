package com.badlyac.forgemod.AutoRun;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;

public class AutoRunHandler {
    private static boolean isRunning = true;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getMinecraft().gameSettings.keyBindSprint.isPressed()) {
            isRunning = !isRunning;
            Minecraft mc = Minecraft.getMinecraft();
            if (isRunning) {
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "AutoRun Enabled"));
            } else {
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "AutoRun disabled"));
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            if (isRunning && mc.gameSettings.keyBindForward.isKeyDown()) {
                if  (!mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.setSprinting(true);
                }
            }
        }
    }
}