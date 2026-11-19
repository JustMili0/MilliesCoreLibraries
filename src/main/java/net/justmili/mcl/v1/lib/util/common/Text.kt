package net.justmili.mcl.v1.lib.util.common

import net.justmili.mcl.v1.lib.util.server.Servers
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

@Suppress("UNUSED")
object Text {

    @JvmStatic
    fun empty() = ""

    @JvmStatic
    fun text(string: String, vararg args: Any?): MutableComponent = Component.literal(string.format(*args))

    @JvmStatic
    fun obfuscate(string: String, vararg args: Any?): MutableComponent = Component.literal(string.format(*args)).withStyle(ChatFormatting.OBFUSCATED)

    @JvmStatic
    fun listToString(list: List<String>) = list.joinToString(", ")

    @JvmStatic
    fun translate(string: String, vararg args: Any?): MutableComponent = Component.translatable(string.format(*args))

    //? if >= 1.20.3 {
    /*@JvmStatic
    fun safeTranslate(string: String, vararg args: Any?): MutableComponent = Component.translatableEscape(string, *args)*/
    //?}

    @JvmStatic
    fun translateWithFallback(string: String, fallback: String): MutableComponent = Component.translatableWithFallback(string, fallback, *TranslatableContents.NO_ARGS)

    @JvmStatic
    fun translateWithFallback(string: String, fallback: String, vararg args: Any?): MutableComponent = Component.translatableWithFallback(string, fallback, *args)

    @JvmStatic
    fun sendMessage(player: ServerPlayer, string: Component, overlay: Boolean = false) {
        player.sendSystemMessage(string, overlay)
    }

    @JvmStatic
    fun sendMessage(player: ServerPlayer, string: String, overlay: Boolean = false) {
        sendMessage(player, Component.literal(string), overlay)
    }

    @JvmStatic
    fun sendFailTo(player: ServerPlayer, string: String, overlay: Boolean = false) {
        sendMessage(player, Component.literal(string).withStyle(ChatFormatting.RED), overlay)
    }

    @JvmStatic
    fun broadcastServer(server: MinecraftServer, string: Component, overlay: Boolean = false) {
        Servers.broadcast(server, string, overlay)
    }

    @JvmStatic
    fun broadcastServer(server: MinecraftServer, string: String, overlay: Boolean = false) {
        broadcastServer(server, Component.literal(string), overlay)
    }
}
