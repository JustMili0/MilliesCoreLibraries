package net.justmili.mcl.v2.lib.config

enum class ConfigTypes {
    /**
     * Client-only configuration.
     * 
     * 
     * Exists only on the client and is never synchronized with
     * or modified by the server.
     */
    CLIENT("client"),

    /**
     * Server-only configuration.
     * 
     * 
     * Exists only on the server and is never synchronized with
     * or modified by clients.
     */
    SERVER("server"),

    /**
     * Common configuration available on both the client and server.
     * 
     * 
     * Client and server each use their own local values,
     * so they may differ without being synchronized.
     */
    COMMON("common"),

    /**
     * Common configuration where the client's values take priority.
     * 
     * 
     * 
     * Both client and server have a local configuration file, but when
     * connected, the client's values override the server's values in
     * cases where this is used.
     * 
     * 
     * 
     * Recommended only for uses where a client tells the server what it wants
     * or where a lack of client-side installation defaults to server's values
     * being used for the client.
     * 
     * 
     * 
     * File name matches COMMON unless a COMMON config already exists.
     * Config values synchronize temporarily when used.
     */
    COMMON_CLIENT_PRIORITY("common", "common-client"),

    /**
     * Common configuration where the server's values take priority.
     * 
     * 
     * Both client and server have a local configuration file, but when
     * connected, the server's values override the client's values in memory.
     * 
     * 
     * 
     * Recommended only for uses where some settings require to be enabled
     * on both client and server.
     * 
     * 
     * 
     * File name matches COMMON unless a COMMON config already exists.
     * Config values synchronize at join.
     */
    COMMON_SERVER_PRIORITY("common", "common-server");

    private val suffix: String
    private val altSuffix: String

    constructor(name: String) {
        this.suffix = name
        this.altSuffix = name
    }

    constructor(name: String, altName: String) {
        this.suffix = name
        this.altSuffix = altName
    }

    fun suffix(): String {
        return suffix
    }

    fun altSuffix(): String {
        return altSuffix
    }
}
