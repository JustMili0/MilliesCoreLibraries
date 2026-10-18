package net.justmili.corelibs;

import net.fabricmc.api.ClientModInitializer;
import net.justmili.corelibs.api.events.bridge.fabric.ClientEventsBridge;
import net.justmili.corelibs.config.sync.SyncCommonServerPriority;
import net.justmili.corelibs.core.config.Config;

public class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEventsBridge.init();
        SyncCommonServerPriority.registerClient();

        Config.initClient();
    }
}
