package net.justmili.mcl.v2.lib.config.options

import net.justmili.mcl.CoreLibs
import net.minecraft.resources.ResourceLocation

enum class OptionFlags {
    // dummy test flags that do nothing (to be removed when config lib is finished)
    REQUIRES_DUMMY_FLAG_1,
    REQUIRES_DUMMY_FLAG_2,

    REQUIRES_GAME_RESTART,
    REQUIRES_LEVEL_RELOAD,
    REQUIRES_ASSETS_RELOAD,
    REQUIRES_DATA_RELOAD,
    REQUIRES_RENDERER_RELOAD,
    REQUIRES_RENDERER_UPDATE,
    REQUIRES_VIDEOMODE_RELOAD;

    private val id: ResourceLocation = CoreLibs.asId("config_option_flag." + this.name.lowercase())

    fun getId(): ResourceLocation {
        return id
    }

    fun getDescName(): String {
        return name.lowercase().replace("requires_", "").replace("_", " ")
    }
}