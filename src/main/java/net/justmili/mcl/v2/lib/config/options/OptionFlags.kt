package net.justmili.mcl.v2.lib.config.options

object OptionFlags {
    const val REQUIRES_GAME_RESTART = 1
    const val REQUIRES_LEVEL_RELOAD = 2
    const val REQUIRES_VIDEOMODE_RELOAD = 4
    const val REQUIRES_RENDERER_UPDATE = 8
    const val REQUIRES_RENDERER_RELOAD = 16
    const val REQUIRES_DATAPACK_RELOAD = 32
    const val REQUIRES_ASSETS_RELOAD = 64

    // Names for the "Requires: ..." comments
    internal val NAMES = linkedMapOf(
        REQUIRES_GAME_RESTART to "Game Restart",
        REQUIRES_LEVEL_RELOAD to "Level Reload",
        REQUIRES_VIDEOMODE_RELOAD to "Videomode Reload",
        REQUIRES_RENDERER_UPDATE to "Renderer Update",
        REQUIRES_RENDERER_RELOAD to "Renderer Reload",
        REQUIRES_DATAPACK_RELOAD to "Datapack Reload",
        REQUIRES_ASSETS_RELOAD to "Assets Reload"
    )
}