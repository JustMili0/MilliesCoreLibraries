package net.justmili.mcl.config;

import net.justmili.mcl.v2.lib.config.*;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;
import net.justmili.mcl.v2.lib.config.options.OptionFlags;

public class Config {
    public static MConfigBuilder client = new MConfigBuilder("corelibs", "icons", ConfigTypes.CLIENT, FileTypes.JSON5, true);

    public static ConfigOption<Boolean> alwaysPride;
    public static ConfigOption<Boolean> alwaysDefault;

    public static void initClient() {
        client.create()
            .addCategory("Icon Type", cat -> {
                alwaysPride = cat.addBooleanOption("alwaysUsePrideIcons")
                    .setDefault(false)
                    .setComment("Config Screen icons will always appear as their Pride Month variant   ")
                    .buildOption();
                alwaysDefault = cat.addBooleanOption("alwaysUseDefaultIcons")
                    .setDefault(false)
                    .setFlags(OptionFlags.REQUIRES_DUMMY_FLAG)
                    .setComment("Config Screen icons will always appear as their Default variant.")
                    .buildOption();
            }).build();
    }
}