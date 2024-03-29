package com.badlyac.forgemod.FreeCam;


import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class FreeCamHandler {

    public static KeyBinding FreeCamToggleKey;

    static {
        FreeCamToggleKey = new KeyBinding("Toggle Free Cam", Keyboard.KEY_NONE,"key.categories.gameplay");
        ClientRegistry.registerKeyBinding(FreeCamToggleKey);
    }
}
