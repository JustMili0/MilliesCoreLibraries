package net.justmili.mcl.v1.lib.util.common

import net.justmili.mcl.v1.lib.util.server.Servers
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer

//? if >= 1.21.11 {
/*import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
 *///?}

object CommandUtil {

    // Replace "new" permission system with the good ol' numbers
    //? if >= 1.21.11 {
    /*@JvmStatic
    fun hasPerms(source: CommandSourceStack, level: Int) = source.permissions().hasPermission(Permission.HasCommandLevel(PermissionLevel.byId(level)))
     *///?}

    // Command success/fail response
    @JvmStatic
    @JvmOverloads
    fun sendOk(source: CommandSourceStack, component: Component, allowLogging: Boolean = true) {
        source.sendSuccess({ component }, allowLogging)
    }

    @JvmStatic
    @JvmOverloads
    fun sendOk(source: CommandSourceStack, string: String, allowLogging: Boolean = true) {
        source.sendSuccess({ Component.literal(string) }, allowLogging)
    }

    @JvmStatic
    fun sendFail(source: CommandSourceStack, component: Component) {
        source.sendFailure(component)
    }

    @JvmStatic
    fun sendFail(source: CommandSourceStack, string: String) {
        sendFail(source, Component.literal(string))
    }

    // Other
    @JvmStatic
    fun runAs(source: CommandSourceStack, command: String) {
        Servers.runCommandAs(source, command)
    }

    @JvmStatic
    fun runAsServer(server: MinecraftServer, command: String) {
        Servers.runCommandAsServer(server, command)
    }
}