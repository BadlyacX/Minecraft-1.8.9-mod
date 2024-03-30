package com.badlyac.forgemod.AutoSwitch;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class BlockSwitcher {

    final static KeyBinding autoSwitchKey;

    private boolean isAutoSwitchEnabled = false;

     static {
        autoSwitchKey = new KeyBinding("AutoBlockSwitcher", Keyboard.KEY_NONE, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(autoSwitchKey);
    }
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            if (autoSwitchKey.isPressed()) {
                isAutoSwitchEnabled = !isAutoSwitchEnabled;
            }

            if (isAutoSwitchEnabled) {
                ItemStack heldItemStack = mc.thePlayer.getHeldItem();
                boolean isHeldItemNotWeaponOrToolOrWeb = (heldItemStack == null || heldItemStack.stackSize == 0) ||
                        (!(heldItemStack.getItem() instanceof ItemSword) &&
                                !(heldItemStack.getItem() instanceof ItemTool) &&
                                !(heldItemStack.getItem() instanceof ItemBow) &&
                                !(Block.getBlockFromItem(heldItemStack.getItem()) == Blocks.web));

                if (isHeldItemNotWeaponOrToolOrWeb) {
                    int sameBlockSlot = -1;
                    int anyBlockSlot = -1;

                    for (int i = 0; i < 9; i++) {
                        ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
                        if (itemStack != null && itemStack.getItem() instanceof ItemBlock &&
                                !(Block.getBlockFromItem(itemStack.getItem()) == Blocks.web)) {
                            if (itemStack.isItemEqual(heldItemStack)) {
                                sameBlockSlot = i;
                                break;
                            } else if (anyBlockSlot == -1) {
                                anyBlockSlot = i;
                            }
                        }
                    }

                    if (sameBlockSlot != -1) {
                        mc.thePlayer.inventory.currentItem = sameBlockSlot;
                    } else if (anyBlockSlot != -1 && (heldItemStack == null || heldItemStack.stackSize == 0)) {
                        mc.thePlayer.inventory.currentItem = anyBlockSlot;
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
            mc.thePlayer.addChatMessage(new ChatComponentText(isAutoSwitchEnabled ? EnumChatFormatting.GREEN + "AutoBlockSwitcher enabled" : EnumChatFormatting.RED + "AutoBlockSwitcher disabled"));
        }
    }
}