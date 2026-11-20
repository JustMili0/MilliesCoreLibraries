package net.justmili.mcl.v2.lib.config.sync;

import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v2.lib.util.common.NetUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

    //? if >= 1.20.5 {
/*import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
*///?}

/**
 * Sends the values of one {@link net.justmili.mcl.v2.lib.config.ConfigTypes#COMMON_SERVER_PRIORITY} config from the server to a client, as a JSON string.
 *
 * @param config the sync id of the config
 * @param json   the values of the config
 */
public record SyncPriorityConfigPacket(String config, String json) implements NetUtil.Payload {
    public static final ResourceLocation ID = CoreLibs.asId("sync_priority_config");
    //? if >= 1.20.5 {
    /*public static final Type<SyncPriorityConfigPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPriorityConfigPacket> CODEC = CustomPacketPayload.codec(SyncPriorityConfigPacket::write, SyncPriorityConfigPacket::read);

    @Override
    public Type<SyncPriorityConfigPacket> type() {
        return TYPE;
    }
    *///?}

    public static SyncPriorityConfigPacket read(FriendlyByteBuf buf) {
        return new SyncPriorityConfigPacket(buf.readUtf(SyncPriorityConfigs.MAX_ID_LENGTH), buf.readUtf(SyncPriorityConfigs.MAX_JSON_LENGTH));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(config, SyncPriorityConfigs.MAX_ID_LENGTH);
        buf.writeUtf(json, SyncPriorityConfigs.MAX_JSON_LENGTH);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}