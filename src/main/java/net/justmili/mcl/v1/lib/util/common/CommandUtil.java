package net.justmili.mcl.v1.lib.util.common;

import net.justmili.mcl.v1.lib.util.server.ServerUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class CommandUtil {
    // Replace "new" permission system with the good ol' numbers
    //    public static boolean hasPerms(CommandSourceStack source, int level) {
    //        return source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.byId(level)));
    //    }

    // Command success/fail response
    public static void sendOk(CommandSourceStack source, Component message, boolean allowLogging) {
        source.sendSuccess(() -> message, allowLogging);
    }

    public static void sendOk(CommandSourceStack source, String message, boolean allowLogging) {
        sendOk(source, Component.literal(message), allowLogging);
    }

    public static void sendOk(CommandSourceStack source, Component message) {
        sendOk(source, message, true);
    }

    public static void sendOk(CommandSourceStack source, String message) {
        sendOk(source, message, true);
    }

    public static void sendFail(CommandSourceStack source, Component message) {
        source.sendFailure(message);
    }

    public static void sendFail(CommandSourceStack source, String message) {
        sendFail(source, Component.literal(message));
    }

    // Other
    public static void runCommandAsPlayer(ServerPlayer player, String command) {
        ServerUtil.runCommandAs(player.createCommandSourceStack(), command);
    }

    public static void runCommandAsServer(MinecraftServer server, String command) {
        ServerUtil.runCommandAsServer(server, command);
    }
}
