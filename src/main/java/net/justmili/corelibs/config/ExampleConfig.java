package net.justmili.corelibs.config;

import net.justmili.corelibs.v1.config.ConfigType;
import net.justmili.corelibs.v1.config.FileType;
import net.justmili.corelibs.v1.config.MConfigBuilder;
import net.justmili.corelibs.v1.config.entry.ConfigEntry;
import net.justmili.corelibs.v1.config.entry.ListConfigEntry;

import java.util.List;

public class ExampleConfig {
    // Leave your builders exposed if you want to hook your mod to Mod Menu or (Neo)Forge Config...
    public static MConfigBuilder server = new MConfigBuilder("examplemod", ConfigType.SERVER, FileType.JSON5, true);
    public static MConfigBuilder client = new MConfigBuilder("examplemod", ConfigType.CLIENT, FileType.JSON5, true);

    public static ConfigEntry<Integer> someInt;
    public static ConfigEntry<Float> someFloat;
    public static ConfigEntry<Double> someDouble;
    public static ConfigEntry<Long> someLong;
    public static ConfigEntry<Boolean> someBool;
    public static ConfigEntry<String> someString;
    public static ListConfigEntry<Double> someDoubleList;
    public static ListConfigEntry<String> someStringList;

    public static void register() {
        // ...Or keep it in the register method if you'd like
        // (but you won't be able to integrate your mod with Mod Menu/(Neo)Forge Config)
        // MConfigBuilder server = new MConfigBuilder("examplemod", "suffix-or-suffix", FileType.JSON5, true);

        someInt = server.comment("Integer entry comment")
            .define("someInt", 10, 0, 100);

        server.openCat("Decimal Numbers");
        someFloat = server.comment("Float entry comment")
            .define("someFloat", 1.0f, 0.1f, 5.0f);

        someDouble = server.comment("Double entry comment")
            .define("someDouble", 2.5, 0.0, 20.0);

        someLong = server.comment("Long entry comment")
            .define("someLong", 123456789L, Long.MIN_VALUE, Long.MAX_VALUE);
        server.closeCat();

        someBool = server.comment("Boolean entry comment")
            .define("someBool", true);

        server.comment("A category comment").openCat("Strings");
        someString = server.comment("String entry comment")
            .define("someString", "Awawawa");

        server.openCat("Strings Category");
        someStringList = server.comment("List entry comment - A string list")
            .defineList("someStringList", List.of("minecraft:grass_block", "minecraft:dirt", "minecraft:stone"));
        server.closeCat();

        server.closeCat();

        someDoubleList = server.comment("List entry comment - A double list")
            .defineList("someDoubleList", List.of(4.2, 6.9, 6.7));

        server.build(); // Will create, load and update the config file depending on the situation

        // You can also create configs like so, but they will not be accessible throughout the code of your mod
        ConfigEntry<Integer> somethingForTheClientToBuild = client.define("clientInt", 69, 0, 70);

        client.build();
    }
}