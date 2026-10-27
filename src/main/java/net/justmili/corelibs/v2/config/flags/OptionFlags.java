package net.justmili.corelibs.v2.config.flags;

import net.justmili.corelibs.CoreLibs;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public enum OptionFlags {
    REQUIRES_GAME_RESTART("game restart"),
    REQUIRES_LEVEL_RELOAD("level reload"),
    REQUIRES_ASSETS_RELOAD("assets reload"),
    REQUIRES_DATA_RELOAD("datapack reload"),
    REQUIRES_RENDERER_RELOAD("renderer reload"),
    REQUIRES_RENDERER_UPDATE("renderer update"),
    REQUIRES_VIDEOMODE_RELOAD("videomode reload");

    private final ResourceLocation id = CoreLibs.asId("config_option_flag." + this.name().toLowerCase(Locale.ROOT));
    private final String descName;

    OptionFlags(String name) {
        this.descName = name;
    }

    public ResourceLocation id() {
        return id;
    }

    public String descName() {
        return descName;
    }
}
