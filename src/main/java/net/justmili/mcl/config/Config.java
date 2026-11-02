package net.justmili.mcl.config;

import net.justmili.mcl.v2.lib.config.*;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;

public class Config {
    public static MConfigBuilder client = new MConfigBuilder("corelibs", null, ConfigTypes.CLIENT, FileTypes.JSON5, true);

    public static ConfigOption<Boolean> alwaysPride;
    public static ConfigOption<Boolean> alwaysDefault;

    public static void initClient() {
        client.create(root -> {
            root.addCategory("Icons", cat0 -> {
                alwaysPride = cat0.addBooleanOption("alwaysUsePrideIcons")
                    .setDefault(false)
                    .addComment("Config Screen icons will always appear as their Pride Month variant   ")
                    .buildOption();
                alwaysDefault = cat0.addBooleanOption("alwaysUseDefaultIcons")
                    .setDefault(false)
                    .addComment("Config Screen icons will always appear as their Default variant.")
                    .buildOption();
            });
        }).build();
    }
}