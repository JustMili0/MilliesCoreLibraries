package net.justmili.mcl.v2.lib.config

enum class ConfigTypes(val type: String) {
    /**
     * Client-only configuration.
     *
     * Only exists on the client. Never synchronized with or modified by the server.
     *
     * File name ends in `client`.
     */
    CLIENT("client"),

    /**
     * Server-only configuration.
     *
     * Only exists on the server. Never synchronized with or modified by clients.
     *
     * File name ends in `server`.
     */
    SERVER("server"),

    /**
     * Common configuration available on both client and server.
     *
     * Both sides have their own file and use their own values,
     * so the values can differ. Nothing is synchronized.
     *
     * File name ends in `common`.
     */
    COMMON("common"),

    /**
     * Common configuration for settings that only affect what a player sees or hears.
     *
     * Both sides have their own file, but the server never reads or overrides the client's values.
     * Clients with the mod apply their own values, everyone else gets the server's values.
     *
     * Recommended only for settings that don't change gameplay, like particles and sound volume.
     *
     * File name ends in `common-client`.
     */
    COMMON_CLIENT_PRIORITY("common-client"),

    /**
     * Common configuration where the server's values take priority.
     *
     * Both sides have their own file, but when connected, the server's values
     * override the client's values in memory (the client's file is not modified).
     * Values are synchronized when joining.
     *
     * Recommended only for settings that have to match on both client and server.
     *
     * File name ends in `common-server`.
     */
    COMMON_SERVER_PRIORITY("common-server");
}