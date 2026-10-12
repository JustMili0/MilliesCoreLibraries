package net.justmili.corelibs.forge;

import net.justmili.api.events.bridge.forge.ClientEventsBridge;
import net.justmili.corelibs.CommonClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClient {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        ClientEventsBridge.init();
        // TODO: Add SyncConfigCSPNetworking for Forge (Client)

        CommonClient.init();
    }
}
