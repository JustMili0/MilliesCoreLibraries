package net.justmili.corelibs.config;

import net.justmili.corelibs.libs.config.ConfigType;
import net.justmili.corelibs.libs.config.FileType;
import net.justmili.corelibs.libs.config.MConfigBuilder;
import net.justmili.corelibs.libs.config.entry.ConfigEntry;

public class Config {
    public static MConfigBuilder client = new MConfigBuilder("corelibs", ConfigType.CLIENT, FileType.JSON5, false);

    public static ConfigEntry<Boolean> alwaysPride;
    public static ConfigEntry<Boolean> alwaysDefault;

    public static void initClient() {
        var cfg = client;

        cfg.openCat("Icon Type");
        alwaysPride = cfg.comment("Config Screen icons will always appear as their Pride Month variant")
            .define("always_pride_icons", false);
        alwaysDefault = cfg.comment("Config Screen icons will always appear as their Default variant")
            .define("always_default_icons", false);
        cfg.closeCat();

        cfg.build();
    }
}
