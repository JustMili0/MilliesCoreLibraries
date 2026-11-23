package net.justmili.mcl.v2.lib.config.sync;

import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v2.lib.config.ConfigTypes;
import net.justmili.mcl.v2.lib.util.common.NetUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Sends the values of one {@link ConfigTypes#COMMON_SERVER_PRIORITY} config from the server to a client, as a JSON string.
 *
 * @param config the sync id of the config
 * @param json   the values of the config
 */
public record ConfigSyncPacket(String config, String json) implements NetUtil.Payload {
    public static final NetUtil.Channel<ConfigSyncPacket> CHANNEL = new NetUtil.Channel<>(CoreLibs.asId("sync_priority_config"), ConfigSyncPacket::read);
    //? if >= 1.20.5 {
    /*@Override
    public Type<ConfigSyncPacket> type() {
        return CHANNEL.type;
    }
    *///?}

    public static ConfigSyncPacket read(FriendlyByteBuf buf) {
        return new ConfigSyncPacket(buf.readUtf(ConfigSync.MAX_ID_LENGTH), buf.readUtf(ConfigSync.MAX_S2C_SIZE));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(config, ConfigSync.MAX_ID_LENGTH);
        buf.writeUtf(json, ConfigSync.MAX_S2C_SIZE);
    }

    @Override
    public ResourceLocation id() {
        return CHANNEL.id;
    }
}