package net.justmili.corelibs.v2.config.flags;

import net.justmili.corelibs.CoreLibs;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public enum OptionFlags {
    REQUIRES_GAME_RESTART,
    REQUIRES_LEVEL_RELOAD,
    REQUIRES_ASSETS_RELOAD,
    REQUIRES_DATA_RELOAD,
    REQUIRES_RENDERER_RELOAD,
    REQUIRES_RENDERER_UPDATE,
    REQUIRES_VIDEOMODE_RELOAD;

    private final ResourceLocation id = CoreLibs.asId("config_option_flag." + this.name().toLowerCase(Locale.ROOT));

    public ResourceLocation id() {
        return id;
    }

    public String descName() {
        return name().toLowerCase(Locale.ROOT).replace("requires_", "").replace("_", " ");
    }
}
