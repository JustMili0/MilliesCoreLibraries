package net.justmili.corelibs.client;

import net.fabricmc.api.ClientModInitializer;
import net.justmili.corelibs.libs.api.events.bridge.fabric.ClientEventsBridge;
import net.justmili.corelibs.libs.config.sync.SyncCommonServerPriority;
import net.justmili.corelibs.config.Config;

public class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEventsBridge.init();
        SyncCommonServerPriority.registerClient();

        Config.initClient();
    }
}
