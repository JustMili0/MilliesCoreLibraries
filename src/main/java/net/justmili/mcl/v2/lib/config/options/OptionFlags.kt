package net.justmili.mcl.v2.lib.config.options

enum class OptionFlags {
    REQUIRES_GAME_RESTART,
    REQUIRES_LEVEL_RELOAD,
    REQUIRES_ASSETS_RELOAD,
    REQUIRES_DATA_RELOAD,
    REQUIRES_RENDERER_RELOAD,
    REQUIRES_RENDERER_UPDATE,
    REQUIRES_VIDEOMODE_RELOAD;

    val flagId: Int = 1 shl ordinal
}