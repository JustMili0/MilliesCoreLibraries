package net.justmili.mcl.v2.lib.config.sync;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.justmili.mcl.v1.lib.util.client.GameUtil;
import net.justmili.mcl.v2.lib.util.common.NetUtil;

/**
 * Client side of {@link ConfigSync}.
 */
@Environment(EnvType.CLIENT)
public class ConfigSyncClient {

    public static void init() {
        NetUtil.Client.initClientboundPlayReceiver(ConfigSyncPacket.CHANNEL, ConfigSyncClient::receive);
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ConfigSync.clear());
    }

    private static void receive(ConfigSyncPacket packet) {
        // Singleplayer host already has server's values. They are the server
        if (!GameUtil.client().hasSingleplayerServer()) ConfigSync.apply(packet.config(), packet.json());
    }
}