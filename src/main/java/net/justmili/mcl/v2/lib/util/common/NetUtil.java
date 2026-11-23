package net.justmili.mcl.v2.lib.util.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justmili.mcl.v1.lib.util.common.TickUtil;
import net.justmili.mcl.v1.lib.util.server.Servers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

    //? if >= 1.20.5 {
/*import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
*///?} else {
import io.netty.buffer.Unpooled;
    //?}

public class NetUtil {

    // Whether the client has the mod. Players without it can't receive the packet
    public static boolean canSend(ServerPlayer player, Payload payload) {
        return ServerPlayNetworking.canSend(player, /*? if >= 1.20.5 {*//*payload.type()*//*?} else {*/payload.id()/*?}*/);
    }

    public static void send(ServerPlayer player, Payload payload) {
        ServerPlayNetworking.send(player, /*? if >= 1.20.5 {*//*payload*//*?} else {*/payload.id(), buffer(payload)/*?}*/);
    }

    public static boolean trySend(ServerPlayer player, Payload payload) {
        if (!canSend(player, payload)) return false;
        send(player, payload);
        return true;
    }

    // Sends a packet to everyone on the server. Safe to call from any thread
    public static void sendToAll(MinecraftServer server, Payload payload) {
        TickUtil.serverTickExecute(server, () -> {
            for (var player : Servers.getPlayers(server)) {
                trySend(player, payload);
            }
        });
    }

    //? if < 1.20.5 {
    private static FriendlyByteBuf buffer(Payload payload) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        payload.write(buf);
        return buf;
    }
    //?}

    /**
     * Any-version packet payload. Extends {@code CustomPacketPayload} since 1.20.5.
     */
    public interface Payload /*? if >= 1.20.5 {*//*extends CustomPacketPayload *//*?}*/ {
        ResourceLocation id();

        void write(FriendlyByteBuf buf);
    }

    public static final class Channel<T extends Payload> {
        public final ResourceLocation id;
        private final Function<FriendlyByteBuf, T> reader;
        //? if >= 1.20.5 {
        /*public final CustomPacketPayload.Type<T> type;
        private final StreamCodec<RegistryFriendlyByteBuf, T> codec;
        *///?}

        public Channel(ResourceLocation id, Function<FriendlyByteBuf, T> reader) {
            this.id = id;
            this.reader = reader;
            //? if >= 1.20.5 {
            /*this.type = new CustomPacketPayload.Type<>(id);
            this.codec = CustomPacketPayload.codec(Payload::write, reader::apply);
            *///?}
        }

        public void initClientboundPayload() {
            /*? if >= 1.20.5 {*//*PayloadTypeRegistry.playS2C().register(type, codec);*//*?}*/
        }

        public void initServerboundPayload() {
            /*? if >= 1.20.5 {*//*PayloadTypeRegistry.playC2S().register(type, codec);*//*?}*/
        }

        /**
         * Registers handler for client-to-server packets
         */
        public void initServerboundPlayReceiver(BiConsumer<ServerPlayer, T> handler) {
            ServerPlayNetworking.registerGlobalReceiver(/*? if >= 1.20.5 {*//*type, (payload, context)*//*?} else {*/id, (server, player, listener, buf, sender)/*?}*/ -> /*? if >= 1.20.5 {*//*context.server().execute(() -> handler.accept(context.player(), payload))*//*?} else {*/{
                var payload = reader.apply(buf);
                server.execute(() -> handler.accept(player, payload));
            }/*?}*/);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Client {

        // Whether the server has the mod. Packets can't be sent to servers without it
        public static <T extends Payload> boolean canSend(Channel<T> channel) {
            return ClientPlayNetworking.canSend(/*? if >= 1.20.5 {*//*channel.type*//*?} else {*/channel.id/*?}*/);
        }

        public static <T extends Payload> void send(Channel<T> channel, T payload) {
            ClientPlayNetworking.send(/*? if >= 1.20.5 {*//*payload*//*?} else {*/channel.id, buffer(payload)/*?}*/);
        }

        /**
         * Registers handler for server-to-client packets
         */
        public static <T extends Payload> void initClientboundPlayReceiver(Channel<T> channel, Consumer<T> handler) {
            ClientPlayNetworking.registerGlobalReceiver(/*? if >= 1.20.5 {*//*channel.type, (payload, context)*//*?} else {*/channel.id, (client, listener, buf, sender)/*?}*/ -> /*? if >= 1.20.5 {*//*handler.accept(payload)*//*?} else {*/{
                var payload = channel.reader.apply(buf);
                client.execute(() -> handler.accept(payload));
            }/*?}*/);
        }
    }
}