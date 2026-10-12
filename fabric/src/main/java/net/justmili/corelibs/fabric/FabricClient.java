package net.justmili.corelibs.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.justmili.corelibs.CommonClient;
import net.justmili.corelibs.api.events.bridge.fabric.ClientEventsBridge;
import net.justmili.corelibs.config.sync.fabric.SyncConfigCSPNetworking;

public class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEventsBridge.init();
        SyncConfigCSPNetworking.registerClient();

        CommonClient.init();
    }
}
