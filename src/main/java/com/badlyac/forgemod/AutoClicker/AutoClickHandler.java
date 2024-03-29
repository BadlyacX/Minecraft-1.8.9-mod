package com.badlyac.forgemod.AutoClicker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class AutoClickHandler {
    private static boolean autoClickEnabled = false;
    private static long lastClickTime = 0;
    private static final KeyBinding autoClickKey;

    static {
        autoClickKey = new KeyBinding("Toggle Auto Clicker", Keyboard.KEY_NONE, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(autoClickKey);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (autoClickEnabled) {
            Item heldItem = mc.thePlayer.getHeldItem() != null ? mc.thePlayer.getHeldItem().getItem() : null;
            boolean isForbiddenItem = heldItem != null &&
                    (heldItem instanceof ItemBow || heldItem instanceof ItemFishingRod || heldItem instanceof ItemTool);

            if (!isForbiddenItem) {
                if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                    KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (autoClickKey.isPressed()) {
            autoClickEnabled = !autoClickEnabled;
            mc.thePlayer.addChatMessage(new ChatComponentText(autoClickEnabled ? EnumChatFormatting.GREEN + "Auto clicker enabled" : EnumChatFormatting.RED + "Auto clicker disabled"));
        }
    }
}