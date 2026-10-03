package net.justmili.corelibs.forge;

import net.justmili.api.events.bridge.forge.ServerEventsBridge;
import net.justmili.corelibs.CoreLibs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CoreLibs.ID)
public final class CoreLibsForge {
    public static IEventBus EVENT_BUS;

    public CoreLibsForge(FMLJavaModLoadingContext modContext) {
        MinecraftForge.EVENT_BUS.register(this);
        EVENT_BUS = modContext.getModEventBus();
        ServerEventsBridge.init();
        // TODO: Add SyncConfigCSPNetworking for Forge (Server)

        CoreLibs.init();
    }

    @SubscribeEvent
    static void test(PlayerEvent event) {
    }
}