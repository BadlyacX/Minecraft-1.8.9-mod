package com.badlyac.forgemod.NightVision;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class NightVision {
    private static boolean isNightVisionEnabled = true;
    final static float originalGamma;
    final static KeyBinding toggleNightVision;

    static {
        originalGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
        toggleNightVision = new KeyBinding("Toggle Night vision", Keyboard.KEY_NONE, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(toggleNightVision);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Minecraft.getMinecraft().gameSettings.gammaSetting = 100.0F;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (toggleNightVision.isPressed()) {
            isNightVisionEnabled = !isNightVisionEnabled;
            Minecraft mc = Minecraft.getMinecraft();
            if (isNightVisionEnabled) {
                mc.gameSettings.gammaSetting = 100.0F;
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "NightVision Enabled"));
            } else {
                mc.gameSettings.gammaSetting = originalGamma;
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "NightVision Disabled"));
            }
        }
    }
}