package com.badlyac.forgemod;

import com.badlyac.forgemod.Aim.AutoAim;
import com.badlyac.forgemod.AFK.AFKBot;
import com.badlyac.forgemod.AutoClicker.AutoClickHandler;
import com.badlyac.forgemod.AutoRun.AutoRunHandler;
import com.badlyac.forgemod.AutoSwitch.BlockSwitcher;
import com.badlyac.forgemod.NightVision.NightVision;
import com.badlyac.forgemod.PvP.AutoPvPHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;


@SuppressWarnings("ALL")
@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main {
    public static final String MODID = "automaticmod";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new AutoRunHandler());
        MinecraftForge.EVENT_BUS.register(new NightVision());
        MinecraftForge.EVENT_BUS.register(new AutoAim());
        MinecraftForge.EVENT_BUS.register(new AutoClickHandler());
        MinecraftForge.EVENT_BUS.register(new AFKBot());
        MinecraftForge.EVENT_BUS.register(new AutoPvPHandler());
        MinecraftForge.EVENT_BUS.register(new BlockSwitcher());
    }
}