package net.justmili.corelibs.config;

//import net.justmili.corelibs.v1.config.ConfigType;
//import net.justmili.corelibs.v1.config.FileType;
//import net.justmili.corelibs.v1.config.MConfigBuilder;
//import net.justmili.corelibs.v1.config.entry.ConfigEntry;
import net.justmili.corelibs.v2.config.MConfigInitializer;
import net.justmili.corelibs.v2.config.MConfigBuilder;
import net.justmili.corelibs.v2.config.flags.ConfigTypes;
import net.justmili.corelibs.v2.config.flags.FileTypes;

//public class Config {
//    public static MConfigBuilder client = new MConfigBuilder("corelibs", ConfigType.CLIENT, FileType.JSON5, false);
public class Config implements MConfigInitializer {
    public static MConfigBuilder client = new MConfigBuilder("corelibs", ConfigTypes.CLIENT, FileTypes.JSON5, true);

//    public static ConfigEntry<Boolean> alwaysPride;
//    public static ConfigEntry<Boolean> alwaysDefault;
    public static boolean alwaysPride;
    public static boolean alwaysDefault;

    public void initClient() {
//        var cfg = client;
//
//        cfg.openCat("Icon Type");
//        alwaysPride = cfg.comment("Config Screen icons will always appear as their Pride Month variant")
//            .define("always_pride_icons", false);
//        alwaysDefault = cfg.comment("Config Screen icons will always appear as their Default variant")
//            .define("always_default_icons", false);
//        cfg.closeCat();
//
//        cfg.build();

        client.create(
            // Options and Categories
        );
    }
}
