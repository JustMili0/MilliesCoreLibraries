package net.justmili.mcl.v2.lib.config

enum class FileTypes(private val extension: String) {
    JSON(".json"),
    JSON5(".json5"),
    PROPERTIES(".properties"),
    TOML(".toml");

    fun extension(): String {
        return extension
    }
}