package net.justmili.corelibs.config.sync.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justmili.corelibs.CoreLibs;
import net.justmili.corelibs.config.sync.SyncConfigCSP;
import net.justmili.corelibs.config.sync.SyncConfigCSP.SyncCSPPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncConfigCSPNetworking {
    public static final ResourceLocation CHANNEL = CoreLibs.asId("sync_common_sp_config");

    public static void registerServer() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            List<SyncCSPPayload> payloads = SyncConfigCSP.buildAllPayloads();
            if (payloads.isEmpty()) return;

            FriendlyByteBuf buf = PacketByteBufs.create();
            writePayloads(buf, payloads);
            ServerPlayNetworking.send(handler.player, CHANNEL, buf);
        });
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL, (client, handler, buf, responseSender) -> {
            List<SyncCSPPayload> payloads = readPayloads(buf);
            client.execute(() -> SyncConfigCSP.applyPayloads(payloads));
        });
    }

    private static void writePayloads(FriendlyByteBuf buf, List<SyncCSPPayload> payloads) {
        buf.writeVarInt(payloads.size());
        for (SyncCSPPayload payload : payloads) {
            buf.writeUtf(payload.modId());
            writeMap(buf, payload.entries());
            writeMap(buf, payload.lists());
        }
    }

    private static List<SyncCSPPayload> readPayloads(FriendlyByteBuf buf) {
        int count = buf.readVarInt();
        List<SyncCSPPayload> payloads = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String modId = buf.readUtf();
            Map<String, String> entries = readMap(buf);
            Map<String, String> lists = readMap(buf);
            payloads.add(new SyncCSPPayload(modId, entries, lists));
        }
        return payloads;
    }

    private static void writeMap(FriendlyByteBuf buf, Map<String, String> map) {
        buf.writeVarInt(map.size());
        for (Map.Entry<String, String> mapEntry : map.entrySet()) {
            buf.writeUtf(mapEntry.getKey());
            buf.writeUtf(mapEntry.getValue());
        }
    }

    private static Map<String, String> readMap(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<String, String> map = new HashMap<>((int) (size / 0.75f) + 1);
        for (int i = 0; i < size; i++) {
            String key = buf.readUtf();
            String value = buf.readUtf();
            map.put(key, value);
        }
        return map;
    }
}