package net.justmili.mcl.v2.lib.config.options

import net.justmili.mcl.CoreLibs
import net.minecraft.resources.ResourceLocation

enum class OptionFlags {
    REQUIRES_GAME_RESTART,
    REQUIRES_LEVEL_RELOAD,
    REQUIRES_ASSETS_RELOAD,
    REQUIRES_DATA_RELOAD,
    REQUIRES_RENDERER_RELOAD,
    REQUIRES_RENDERER_UPDATE,
    REQUIRES_VIDEOMODE_RELOAD;

    val flag: Int = 1 shl ordinal
    val id: ResourceLocation = CoreLibs.asId("config_option_flag." + name.lowercase())
}