package net.justmili.mcl.client;

import net.fabricmc.api.ClientModInitializer;
import net.justmili.mcl.v1.api.events.bridge.ClientEventsBridge;
import net.justmili.mcl.v2.lib.config.sync.ConfigSyncClient;

public class FabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientEventsBridge.init();
        ConfigSyncClient.init();
    }
}