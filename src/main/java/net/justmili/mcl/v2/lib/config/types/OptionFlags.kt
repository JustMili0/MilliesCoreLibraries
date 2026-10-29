package net.justmili.mcl.v2.lib.config

import net.justmili.mcl.CoreLibs
import net.minecraft.resources.ResourceLocation

enum class OptionFlags {
    REQUIRES_DUMMY_FLAG, // dummy test flag that does nothing
    REQUIRES_GAME_RESTART,
    REQUIRES_LEVEL_RELOAD,
    REQUIRES_ASSETS_RELOAD,
    REQUIRES_DATA_RELOAD,
    REQUIRES_RENDERER_RELOAD,
    REQUIRES_RENDERER_UPDATE,
    REQUIRES_VIDEOMODE_RELOAD;

    private val id: ResourceLocation = CoreLibs.asId("config_option_flag." + this.name.lowercase())

    fun id(): ResourceLocation {
        return id
    }

    fun descName(): String {
        return name.lowercase().replace("requires_", "").replace("_", " ")
    }
}