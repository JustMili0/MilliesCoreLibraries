package net.justmili.corelibs.util.common;

import net.justmili.corelibs.util.server.ServerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class Text {

    public static String empty() {
        return "";
    }

    public static MutableComponent text(String text, Object... args) {
        return Component.literal(String.format(text, args));
    }

    public static MutableComponent obfuscate(String text, Object... args) {
        return Component.literal(String.format(text, args)).withStyle(ChatFormatting.OBFUSCATED);
    }

    public static MutableComponent toList(String text) {
        return Component.literal(text);
    }

    public static MutableComponent translate(String key, Object... args) {
        return Component.translatable(String.format(key, args));
    }

//    public static MutableComponent translateSafe(String key, Object... args) {
//        return Component.translatableEscape(key, args);
//    }

    public static MutableComponent translateWithFallback(String key, String fallback) {
        return Component.translatableWithFallback(key, fallback, TranslatableContents.NO_ARGS);
    }

    public static MutableComponent translateWithFallback(String key, String fallback, Object... args) {
        return Component.translatableWithFallback(key, fallback, args);
    }

    public static void sendOkTo(ServerPlayer player, Component message, boolean showAboveHotbar) {
        player.sendSystemMessage(message, showAboveHotbar);
    }

    public static void sendOkTo(ServerPlayer player, String message, boolean showAboveHotbar) {
        sendOkTo(player, Component.literal(message), showAboveHotbar);
    }

    public static void sendOkTo(ServerPlayer player, Component message) {
        player.sendSystemMessage(message, false);
    }

    public static void sendOkTo(ServerPlayer player, String message) {
        sendOkTo(player, Component.literal(message), false);
    }

    public static void sendFailTo(ServerPlayer player, String message, boolean showAboveHotbar) {
        sendOkTo(player, Component.literal("§c" + message), showAboveHotbar);
    }

    public static void sendFailTo(ServerPlayer player, String message) {
        sendOkTo(player, Component.literal("§c" + message), false);
    }

    public static void broadcastServer(MinecraftServer server, Component message, boolean showAboveHotbar) {
        ServerUtil.broadcast(server, message, showAboveHotbar);
    }

    public static void broadcastServer(MinecraftServer server, String message, boolean showAboveHotbar) {
        broadcastServer(server, Component.literal(message), showAboveHotbar);
    }

    public static void broadcastServer(MinecraftServer server, Component message) {
        ServerUtil.broadcast(server, message, false);
    }

    public static void broadcastServer(MinecraftServer server, String message) {
        broadcastServer(server, Component.literal(message), false);
    }
}
