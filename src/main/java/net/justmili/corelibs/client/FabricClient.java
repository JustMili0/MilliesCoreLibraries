package net.justmili.corelibs.client;

import net.fabricmc.api.ClientModInitializer;
import net.justmili.corelibs.config.Config;
import net.justmili.corelibs.libs.config.sync.SyncPriorityConfigs;

public class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEventsBridge.init();
        SyncPriorityConfigs.registerClient();

        Config.initClient();
    }
}
