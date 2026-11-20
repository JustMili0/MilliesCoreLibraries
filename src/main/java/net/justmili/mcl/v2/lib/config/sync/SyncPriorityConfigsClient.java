package net.justmili.mcl.v2.lib.config.sync;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.justmili.mcl.v1.lib.util.client.GameUtil;
import net.justmili.mcl.v2.lib.util.common.NetUtil;

/**
 * Client side of {@link SyncPriorityConfigs}.
 */
@Environment(EnvType.CLIENT)
public class SyncPriorityConfigsClient {

    public static void init() {
        NetUtil.Client.onS2C(/*? if >= 1.20.5 {*//*SyncPriorityConfigPacket.TYPE*//*?} else {*/SyncPriorityConfigPacket.ID, SyncPriorityConfigPacket::read/*?}*/, SyncPriorityConfigsClient::receive);
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> SyncPriorityConfigs.clear());
    }

    private static void receive(SyncPriorityConfigPacket packet) {
        // The host of a singleplayer world already has the server's values
        if (!GameUtil.client().hasSingleplayerServer()) SyncPriorityConfigs.apply(packet.config(), packet.json());
    }
}