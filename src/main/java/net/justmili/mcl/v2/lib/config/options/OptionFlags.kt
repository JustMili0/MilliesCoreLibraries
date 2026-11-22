package net.justmili.mcl.v2.lib.config.options

/**
 * Option flags define what the has to happen for the new value of the flagged option to take effect.
 */
enum class OptionFlags(val configName: String) {
    REQUIRES_GAME_RESTART("Game Restart"),
    REQUIRES_LEVEL_RELOAD("Level Reload"),
    REQUIRES_VIDEOMODE_RELOAD("Videomode Reload"),
    REQUIRES_RENDERER_UPDATE("Renderer Update"),
    REQUIRES_RENDERER_RELOAD("Renderer Reload"),
    REQUIRES_DATAPACK_RELOAD("Datapacks Reload"),
    REQUIRES_ASSETS_RELOAD("Assets Reload");

    // ID by bit
    val id = 1 shl ordinal
}