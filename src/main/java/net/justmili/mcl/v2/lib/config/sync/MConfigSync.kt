package net.justmili.mcl.v2.lib.config.sync

import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import io.netty.buffer.ByteBufUtil
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.justmili.mcl.v1.lib.util.client.GameUtil
import net.justmili.mcl.v2.api.event.common.MConfigEvents
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.codecs.ConfigCodec
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.util.common.NetUtil
import net.minecraft.server.MinecraftServer
import java.util.concurrent.CopyOnWriteArrayList

//? if >= 1.20.5 {
/*import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
*///?} else {
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket
//?}

class MConfigSync(val id: String, private val root: CategoryItem, private val lock: Any) {
    private var sent: String? = null

    init {
        SYNCS.add(this)
    }

    fun toJson(): String? = synchronized(lock) { encode() }

    fun apply(json: String) {
        val tree = try {
            JsonParser.parseString(json)
        } catch (e: JsonParseException) {
            MConfigLogger.syncFail(id, e.message)
            return
        }
        ConfigCodec.of(root, true).parse(JsonOps.INSTANCE, tree).error().ifPresent { error -> MConfigLogger.syncFail(id, error.message()) }
    }

    // Goes back to the client's own values
    fun clear() = root.getChildren().forEach { option -> option.clearSynced() }

    // Sends the current values of the config to everyone online
    fun sendToAll(server: MinecraftServer?) {
        if (server == null) return
        val json = synchronized(lock) { encode()?.takeIf { it != sent }?.also { sent = it } } ?: return
        val packet = packetOf(json) ?: return

        NetUtil.sendToAll(server, packet)
        MConfigEvents.SYNC_ALL_CLIENTBOUND.invoker().onSync(this, server)
    }

    private fun encode(): String? {
        val json = ConfigCodec.of(root).encodeStart(JsonOps.INSTANCE, root).resultOrPartial { error -> MConfigLogger.syncFail(id, error) }
        return json.map { it.toString() }.orElse(null)
    }

    private fun packetOf(json: String): ConfigSyncPacket? {
        if (ByteBufUtil.utf8Bytes(json) <= ClientboundCustomPayloadPacket.MAX_PAYLOAD_SIZE) return ConfigSyncPacket(id, json)
        MConfigLogger.syncFail(id, "Over byte limit")
        return null
    }

    companion object {
        const val MAX_ID_LENGTH = 256
        private val SYNCS = CopyOnWriteArrayList<MConfigSync>()

        @JvmStatic
        fun initClientbound() {
            ConfigSyncPacket.CHANNEL.initClientboundPayload()

            // Everything is sent on join, anything after is sent on reload
            ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
                val player = handler.player
                for (sync in SYNCS) {
                    val packet = sync.toJson()?.let { sync.packetOf(it) } ?: continue
                    if (NetUtil.trySend(player, packet)) MConfigEvents.SYNC_CLIENTBOUND.invoker().onSync(sync, player)
                }
            }
        }

        @JvmStatic
        @Environment(EnvType.CLIENT)
        fun initClientboundReceiver() {
            NetUtil.Client.initClientboundPlayReceiver(ConfigSyncPacket.CHANNEL) { packet ->
                // Singleplayer host already has server's values. They are the server
                if (!GameUtil.client().hasSingleplayerServer()) SYNCS.firstOrNull { it.id == packet.config }?.apply(packet.json)
            }
            ClientPlayConnectionEvents.DISCONNECT.register { _, _ -> SYNCS.forEach { it.clear() } }
        }
    }
}