package net.justmili.mcl.v1.lib.util.common

import net.justmili.mcl.v1.lib.util.server.Servers
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

@Suppress("UNUSED")
object Text {

    @JvmStatic
    fun empty() = ""

    @JvmStatic
    fun text(string: String, vararg args: Any?): MutableComponent = Component.literal(parseArgs(string, *args))

    @JvmStatic
    fun obfuscate(string: String, vararg args: Any?): MutableComponent = Component.literal(parseArgs(string, *args)).withStyle(ChatFormatting.OBFUSCATED)

    @JvmStatic
    fun listToString(list: List<String>) = list.joinToString(", ")

    @JvmStatic
    fun translate(string: String, vararg args: Any?): MutableComponent = Component.translatable(string, *args)

    //? if >= 1.20.3 {
    /*@JvmStatic
    fun safeTranslate(string: String, vararg args: Any?): MutableComponent = Component.translatableEscape(string, *args)
    *///?}

    @JvmStatic
    fun translateWithFallback(string: String, fallback: String, vararg args: Any?): MutableComponent = Component.translatableWithFallback(string, fallback, *args)

    @JvmStatic
    @JvmOverloads
    fun sendMessage(player: ServerPlayer, component: Component, overlay: Boolean = false) {
        player.sendSystemMessage(component, overlay)
    }

    @JvmStatic
    @JvmOverloads
    fun sendMessage(player: ServerPlayer, string: String, overlay: Boolean = false) {
        sendMessage(player, Component.literal(string), overlay)
    }

    @JvmStatic
    @JvmOverloads
    fun sendFailTo(player: ServerPlayer, string: String, overlay: Boolean = false) {
        sendMessage(player, Component.literal(string).withStyle(ChatFormatting.RED), overlay)
    }

    @JvmStatic
    @JvmOverloads
    fun broadcastServer(server: MinecraftServer, component: Component, overlay: Boolean = false) {
        Servers.broadcast(server, component, overlay)
    }

    @JvmStatic
    @JvmOverloads
    fun broadcastServer(server: MinecraftServer, string: String, overlay: Boolean = false) {
        broadcastServer(server, Component.literal(string), overlay)
    }

    private fun parseArgs(string: String, vararg args: Any?) = if (args.isEmpty()) string else string.format(*args)
}
