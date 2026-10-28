package net.justmili.corelibs.v2.config;

public enum ConfigTypes {
    /**
     * Client-only configuration.
     * <p>
     * Exists only on the client and is never synchronized with
     * or modified by the server.
     */
    CLIENT("client"),

    /**
     * Server-only configuration.
     * <p>
     * Exists only on the server and is never synchronized with
     * or modified by clients.
     */
    SERVER("server"),

    /**
     * Common configuration available on both the client and server.
     * <p>
     * Client and server each use their own local values,
     * so they may differ without being synchronized.
     */
    COMMON("common"),

    /**
     * Common configuration where the client's values take priority.
     *
     * <p>
     * Both client and server have a local configuration file, but when
     * connected, the client's values override the server's values in
     * cases where this is used.
     *
     * <p>
     * Recommended only for uses where a client tells the server what it wants
     * or where a lack of client-side installation defaults to server's values
     * being used for the client.
     *
     * <p>
     * File name matches COMMON unless a COMMON config already exists.
     * Config values synchronize temporarily when used.
     */
    COMMON_CLIENT_PRIORITY("common", "common-client"),

    /**
     * Common configuration where the server's values take priority.
     * <p>
     * Both client and server have a local configuration file, but when
     * connected, the server's values override the client's values in memory.
     *
     * <p>
     * Recommended only for uses where some settings require to be enabled
     * on both client and server.
     *
     * <p>
     * File name matches COMMON unless a COMMON config already exists.
     * Config values synchronize at join.
     */
    COMMON_SERVER_PRIORITY("common", "common-server");

    private final String suffix;
    private final String altSuffix;

    ConfigTypes(String name) {
        this.suffix = name;
        this.altSuffix = name;
    }

    ConfigTypes(String name, String altName) {
        this.suffix = name;
        this.altSuffix = altName;
    }

    public String suffix() {
        return suffix;
    }

    public String altSuffix() {
        return altSuffix;
    }
}
