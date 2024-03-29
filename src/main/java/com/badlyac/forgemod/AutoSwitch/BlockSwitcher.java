package com.badlyac.forgemod.AutoSwitch;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class BlockSwitcher {

    private static KeyBinding autoSwitchKey;

    private boolean isAutoSwitchEnabled = false;

     static {
        autoSwitchKey = new KeyBinding("AutoBlockSwitcher", Keyboard.KEY_NONE, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(autoSwitchKey);
    }


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (autoSwitchKey.isPressed()) {
            isAutoSwitchEnabled = !isAutoSwitchEnabled;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (isAutoSwitchEnabled) {
            ItemStack heldItemStack = mc.thePlayer.getHeldItem();
            if (heldItemStack == null || heldItemStack.stackSize == 0) {
                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
                    if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                        mc.thePlayer.inventory.currentItem = i;
                        break;
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (autoSwitchKey.isPressed()) {
            isAutoSwitchEnabled = !isAutoSwitchEnabled;
            mc.thePlayer.addChatMessage(new ChatComponentText(isAutoSwitchEnabled ? EnumChatFormatting.GREEN + "Auto Block Switcher enabled" : EnumChatFormatting.RED + "Auto Block Switcher disabled"));
        }
    }
}