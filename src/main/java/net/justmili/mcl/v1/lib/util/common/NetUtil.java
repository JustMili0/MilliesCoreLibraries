package net.justmili.mcl.v1.lib.util.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justmili.mcl.v1.lib.util.server.Servers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

    //? if >= 1.20.5 {
/*import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
*///?} else {
import io.netty.buffer.Unpooled;

import java.util.function.Function;
    //?}

public class NetUtil {

    // Whether the client has the mod. Players without it can't receive the packet
    public static boolean canSend(ServerPlayer player, Payload payload) {
        return ServerPlayNetworking.canSend(player, /*? if >= 1.20.5 {*//*payload.type()*//*?} else {*/payload.id()/*?}*/);
    }

    //? if >= 1.20.5 {
    /*public static <T extends Payload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        PayloadTypeRegistry.playS2C().register(type, codec);
    }
    *///?}

    public static void send(ServerPlayer player, Payload payload) {
        //? if >= 1.20.5 {
        /*ServerPlayNetworking.send(player, payload);
         *///?} else {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        payload.write(buf);
        ServerPlayNetworking.send(player, payload.id(), buf);
        //?}
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

    /**
     * Any-version packet payload. Extends {@code CustomPacketPayload} since 1.20.5.
     */
    public interface Payload /*? if >= 1.20.5 {*//*extends CustomPacketPayload *//*?}*/ {
        ResourceLocation id();

        void write(FriendlyByteBuf buf);
    }

    @Environment(EnvType.CLIENT)
    public static class Client {

        // Handlers run on the client thread
        //? if >= 1.20.5 {
        /*public static <T extends Payload> void onS2C(CustomPacketPayload.Type<T> type, Consumer<T> handler) {
            ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> handler.accept(payload));
        }
        *///?} else {
        public static <T extends Payload> void onS2C(ResourceLocation id, Function<FriendlyByteBuf, T> reader, Consumer<T> handler) {
            ClientPlayNetworking.registerGlobalReceiver(id, (client, listener, buf, sender) -> {
                var payload = reader.apply(buf);
                client.execute(() -> handler.accept(payload));
            });
        }
        //?}
    }
}