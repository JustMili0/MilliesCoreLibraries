package net.justmili.mcl.client;

import net.fabricmc.api.ClientModInitializer;
import net.justmili.mcl.v1.api.event.bridge.ClientEventsBridge;
import net.justmili.mcl.v2.lib.config.sync.MConfigSync;

public class FabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientEventsBridge.init();
        MConfigSync.initClientboundReceiver();
    }
}