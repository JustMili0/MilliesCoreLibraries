package net.justmili.corelibs.config;

import net.justmili.corelibs.v2.config.ConfigTypes;
import net.justmili.corelibs.v2.config.FileTypes;
import net.justmili.corelibs.v2.config.MConfigBuilder;
import net.justmili.corelibs.v2.config.OptionFlags;

public class Config {
    public static MConfigBuilder client = new MConfigBuilder("corelibs", "icons", ConfigTypes.CLIENT, FileTypes.JSON5, true);

    // TODO: Rework config again so it uses "ConfigOption<Boolean> ..." for example
    // It has to work with everything, including enum values, lists etc
    public static boolean alwaysPride;
    public static boolean alwaysDefault;

    public static void initClient() {

        client.create()
            .addCategory("Icon Type", cat -> cat
                .addOption("alwaysUsePrideIcons", false, value -> alwaysPride = value, OptionFlags.REQUIRES_DUMMY_FLAG)
                .comment("Config Screen icons will always appear as their Pride Month variant")
                .addOption("alwaysUseDefaultIcons", false, value -> alwaysDefault = value, OptionFlags.REQUIRES_DUMMY_FLAG)
                .comment("Config Screen icons will always appear as their Default variant")
            ).build();
    }
}