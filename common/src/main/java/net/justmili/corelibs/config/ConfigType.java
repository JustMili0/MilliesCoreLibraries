package net.justmili.corelibs.config;

public enum ConfigType {

    /**
     * Client-only configuration.
     * <p>
     * Exists only on the client and is never synchronized with
     * or modified by the server.
     */
    CLIENT,

    /**
     * Server-only configuration.
     * <p>
     * Exists only on the server and is never synchronized with
     * or modified by clients.
     */
    SERVER,

    /**
     * Common configuration available on both the client and server.
     * <p>
     * Client and server each use their own local values,
     * so they may differ without being synchronized.
     */
    COMMON,

    /**
     * Common configuration where the server's values take priority.
     * <p>
     * Both client and server have a local configuration file, but when
     * connected, the server's values override the client's values in memory.
     */
    COMMON_SERVER_PRIORITY
}