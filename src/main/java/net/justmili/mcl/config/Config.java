package net.justmili.mcl.config;

import net.justmili.mcl.v2.lib.config.*;
import net.justmili.mcl.v2.lib.config.option.OptionFlags;

public class Config {
    public static MConfigBuilder client = new MConfigBuilder("corelibs", "icons", ConfigTypes.CLIENT, FileTypes.PROPERTIES, true);

    // TODO: Rework config again so it uses "ConfigOption<Boolean> ..." for example
    // It has to work with everything, including enum values, lists etc
    public static boolean alwaysPride;
    public static boolean alwaysDefault;

    public static void initClient() {

        client.create()
            .addCategory("Icon Type", cat -> cat
                .addOption("alwaysUsePrideIcons", false, value -> alwaysPride = value, OptionFlags.REQUIRES_DUMMY_FLAG)
                .comment("Config Screen icons will always appear as their Pride Month variant   ")
                .addOption("alwaysUseDefaultIcons", false, value -> alwaysDefault = value, OptionFlags.REQUIRES_DUMMY_FLAG)
                .comment("Config Screen icons will always appear as their Default variant.")
            ).build();
    }
}