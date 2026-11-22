package net.justmili.mcl.v2.lib.config.sync;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v2.lib.util.common.NetUtil;
import net.minecraft.server.MinecraftServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps the server's {@link net.justmili.mcl.v2.lib.config.ConfigTypes#COMMON_SERVER_PRIORITY} configs in sync with the clients. Common side, the client side is in {@link SyncPriorityConfigsClient}.
 */
public class SyncPriorityConfigs {
    static final int MAX_ID_LENGTH = 256;
    static final int MAX_JSON_LENGTH = 1 << 18;
    private static final Map<String, MConfigSync> SYNCS = new ConcurrentHashMap<>();

    public static void register(MConfigSync sync) {
        SYNCS.put(sync.getId(), sync);
    }

    public static void initS2C() {
        /*? if >= 1.20.5 {*//*NetUtil.registerS2C(SyncPriorityConfigPacket.TYPE, SyncPriorityConfigPacket.CODEC);*//*?}*/

        // Everything is sent on join, anything after is sent on reload
        ServerPlayConnectionEvents.JOIN.register((handler, sender, joined) -> {
            var player = handler.getPlayer();
            for (var sync : SYNCS.values()) {
                var packet = packetOf(sync, sync.toJson());
                if (packet != null) NetUtil.trySend(player, packet);
            }
        });
    }

    // Sends the current values of the config to everyone online
    public static void sendToAll(MinecraftServer server, MConfigSync sync) {
        if (server == null) return;
        var packet = packetOf(sync, sync.toChangedJson());
        if (packet != null) NetUtil.sendToAll(server, packet);
    }

    // Applies the values received from the server
    static void apply(String config, String json) {
        var sync = SYNCS.get(config);
        if (sync != null) sync.apply(json);
    }

    // Goes back to the client's own values of all configs
    static void clear() {
        SYNCS.values().forEach(MConfigSync::clear);
    }

    private static SyncPriorityConfigPacket packetOf(MConfigSync sync, String json) {
        if (json == null) return null;
        if (json.length() > MAX_JSON_LENGTH) {
            CoreLibs.LOGGER.error("Could not sync config '{}': too large ({} > {} characters)", sync.getId(), json.length(), MAX_JSON_LENGTH);
            return null;
        }
        return new SyncPriorityConfigPacket(sync.getId(), json);
    }
}