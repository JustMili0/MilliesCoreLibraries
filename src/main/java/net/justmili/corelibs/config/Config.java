package net.justmili.corelibs.config;

import net.justmili.corelibs.v2.config.MConfigInitializer;
import net.justmili.corelibs.v2.config.MConfigBuilder;
import net.justmili.corelibs.v2.config.flags.ConfigTypes;
import net.justmili.corelibs.v2.config.flags.FileTypes;

public class Config implements MConfigInitializer {
    public static MConfigBuilder client = new MConfigBuilder("corelibs", "icons", ConfigTypes.CLIENT, FileTypes.JSON5, true);

    public static boolean alwaysPride;
    public static boolean alwaysDefault;

    @Override
    public void initClient() {

//        client.create().addCategory("Icon Type", client.create() // do smth so you don't constantly use client.create()
//                .addOption("alwaysUsePrideIcons", false)
//                    .comment("Config Screen icons will always appear as their Pride Month variant") // do smth to allow chaining like that
//                .addOption("alwaysUseDefaultIcons", false)
//                    .comment("Config Screen icons will always appear as their Default variant"));
    }
}
