package net.justmili.mcl.v2.lib.config

import net.minecraft.server.MinecraftServer;

/**
 * Config type defines the environment the config is used in and what it's capable of.
 *
 * It also tells what file name or suffix is used in the creation, writing and reading of the config file.
 */
enum class ConfigTypes(val type: String) {

    /**
     * Client-only configuration.
     *
     * Only exists on the client.
     *
     * Never synchronized with or modified by the server.
     *
     * File name ends in `client`.
     */
    CLIENT("client"),

    /**
     * Server-only configuration.
     *
     * Only exists on the [MinecraftServer] (Dedicated, Singleplayer and LAN).
     *
     * Never synchronized with or modified by any client.
     *
     * File name ends in `server`.
     */
    SERVER("server"),

    /**
     * Common configuration available on both client and server.
     *
     * For options that aren't specific to either side.
     * Both sides keep their own values, and which ones are used differs on the environment.
     *
     * Never synchronized across client and server.
     *
     * File name ends in `common`.
     */
    COMMON("common"),

    /**
     * Common configuration where the client's values take priority.
     *
     * Meant for mods with an optional client install.
     *
     * Clients with the mod use their own values, separate from the server's.
     * Clients without it use the server's values instead.
     *
     * The client can send its values to the [MinecraftServer], which then only applies them to that player.
     *
     * File name ends in `common-client`.
     */
    COMMON_CLIENT_PRIORITY("common-client"),

    /**
     * Common configuration where the server's values take priority.
     *
     * Meant for mods that need the same values on both sides, without players having to match their config by hand.
     *
     * The [MinecraftServer] sends its values to the client on join, and sends updates on [MConfigLoader.reload].
     *
     * Synchronized values only replace the client's in memory. The client's file is never changed.
     *
     * File name ends in `common-server`.
     */
    COMMON_SERVER_PRIORITY("common-server");
}