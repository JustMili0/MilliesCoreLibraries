package net.justmili.corelibs.client;

import net.fabricmc.api.ClientModInitializer;
import net.justmili.corelibs.v1.api.events.bridge.ClientEventsBridge;
import net.justmili.corelibs.v1.config.sync.SyncPriorityConfigs;

public class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEventsBridge.init();
        SyncPriorityConfigs.registerClient();
    }
}
