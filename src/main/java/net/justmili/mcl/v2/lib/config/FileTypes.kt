package net.justmili.mcl.v2.lib.config

enum class FileTypes(val extension: String) {

    /**
     * Regular JSON file. Uses `.json` file extension.
     *
     * Each comment line is written as members with a counted key. Categories are supported.
     */
    JSON(".json"),

    /**
     * Regular JSON5 file; [JSON] but with proper comments. Uses `.json5` file extension.
     *
     * Comments are written as `// Comment` lines. Categories are supported.
     */
    JSON5(".json5"),

    /**
     * Regular PROPERTIES file. Uses `.properties` file extension.
     *
     * Comments are written as `# Comment` lines. Categories are not supported.
     */
    PROPERTIES(".properties"),

    /**
     * Regular TOML file. Uses `.toml` file extension.
     *
     * Comments are written as `# Comment` lines. Categories are supported as tables.
     */
    TOML(".toml");
}