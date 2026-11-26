package net.justmili.mcl.v2.lib.config.sync

import net.justmili.mcl.CoreLibs
import net.justmili.mcl.v2.lib.config.ConfigTypes
import net.justmili.mcl.v2.lib.util.common.NetUtil
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation

//? if >= 1.20.5 {
/*import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
*///?} else {
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket
//?}

/**
 * Sends the values of one [ConfigTypes.COMMON_SERVER_PRIORITY] config from the server to a client, as a JSON string.
 *
 * @param config the sync id of the config
 * @param json   the values of the config
 */
data class ConfigSyncPacket(val config: String, val json: String) : NetUtil.Payload {

    //? if >= 1.20.5 {
    /*override fun type(): CustomPacketPayload.Type<ConfigSyncPacket> = CHANNEL.type
    *///?}

    override fun id(): ResourceLocation = CHANNEL.id

    override fun write(buf: FriendlyByteBuf) {
        buf.writeUtf(config, MConfigSync.MAX_ID_LENGTH)
        buf.writeUtf(json, ClientboundCustomPayloadPacket.MAX_PAYLOAD_SIZE)
    }

    companion object {

        @JvmField val CHANNEL = NetUtil.Channel(CoreLibs.asId("sync_priority_config")) { buf: FriendlyByteBuf ->
            ConfigSyncPacket(buf.readUtf(MConfigSync.MAX_ID_LENGTH), buf.readUtf(ClientboundCustomPayloadPacket.MAX_PAYLOAD_SIZE))
        }
    }
}