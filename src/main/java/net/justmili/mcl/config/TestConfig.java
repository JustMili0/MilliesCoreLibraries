package net.justmili.mcl.config;

import net.justmili.mcl.v2.lib.config.*;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;
import net.justmili.mcl.v2.lib.config.options.OptionFlags;
import net.minecraft.util.Mth;

@Deprecated(since = "1.0.0", forRemoval = true)
public class TestConfig {
    public static ConfigOption<Integer> rootInt;
    public static ConfigOption<Integer> rootIntRanged;
    public static ConfigOption<Integer> rootIntRangedNeg;
    public static ConfigOption<Boolean> cat0StartBool;
    public static ConfigOption<Integer> cat0StartInt;
    public static ConfigOption<String> cat0StartString;
    public static ConfigOption<Long> cat0MidLong;
    public static ConfigOption<Float> cat0MidFloat;
    public static ConfigOption<Double> cat0MidDouble;
    public static ConfigOption<Boolean> cat0EndBool;
    public static ConfigOption<Boolean> cat1Bool;
    public static ConfigOption<Integer> cat1Int;
    public static ConfigOption<Long> cat1Long;
    public static ConfigOption<Float> cat1Float;
    public static ConfigOption<Double> cat1Double;
    public static ConfigOption<String> cat1String;
    public static ConfigOption<Boolean> cat2Bool;
    public static ConfigOption<Integer> cat2Int;
    public static ConfigOption<Long> cat2Long;
    public static ConfigOption<Float> cat2Float;
    public static ConfigOption<Double> cat2Double;
    public static ConfigOption<String> cat2String;

    // Function made only for purpose of this example test
    private static MConfigBuilder createBuilder(FileTypes ext) {
        return new MConfigBuilder("examplemod", null, ConfigTypes.COMMON, ext, true);
    }

    public static void initCommon() {
        for (var type : FileTypes.getEntries()) {
            if (type == FileTypes.TOML) continue;
            create(createBuilder(type));
        }
    }

    public static void create(MConfigBuilder builder) {
        builder.create(root -> {
            root.addComment("Config root comment");
            root.addComment("Second root comment\nwith two lines");

            rootInt = root.addIntOption("rootInt")
                .setDefault(10)
                .addComment("Some integer value")
                .buildOption();
            rootIntRanged = root.addIntOption("rootIntRanged")
                .setDefault(10)
                .setRange(0, 99)
                .addComment("Value with flags")
                .addFlags(OptionFlags.REQUIRES_ASSETS_RELOAD, OptionFlags.REQUIRES_DATA_RELOAD)
                .buildOption();
            rootIntRangedNeg = root.addIntOption("rootIntRangedNeg")
                .setDefault(-10)
                .setRange(-99, -1)
                .buildOption();

            root.addCategory("Root Category", cat0 -> {
                cat0StartBool = cat0.addBooleanOption("cat0StartBool")
                    .setDefault(true)
                    .addComment("Some boolean value")
                    .buildOption();
                cat0StartInt = cat0.addIntOption("cat0StartInt")
                    .setDefault(25)
                    .setRange(1, 100)
                    .addComment("Some ranged integer value")
                    .buildOption();
                cat0StartString = cat0.addStringOption("cat0StartString")
                    .setDefault("Hello")
                    .addComment("Some string value")
                    .buildOption();

                cat0.addCategory("Cat-in-cat 1", cat1 -> {
                    cat1Bool = cat1.addBooleanOption("cat1Bool")
                        .setDefault(false)
                        .addComment("Some boolean value")
                        .buildOption();
                    cat1Int = cat1.addIntOption("cat1Int")
                        .setDefault(5)
                        .setRange(0, 10)
                        .addComment("Some ranged integer value")
                        .buildOption();
                    cat1Long = cat1.addLongOption("cat1Long")
                        .setDefault(1000000L)
                        .addComment("Some long value")
                        .buildOption();
                    cat1Float = cat1.addFloatOption("cat1Float")
                        .setDefault(0.69f)
                        .setRange(0f, 1f)
                        .addComment("Some ranged float value")
                        .buildOption();
                    cat1Double = cat1.addDoubleOption("cat1Double")
                        .setDefault(4.2)
                        .addComment("Some double value")
                        .buildOption();
                    cat1String = cat1.addStringOption("cat1String")
                        .setDefault("Short text")
                        .setCharLimit(32)
                        .addComment("Some string value with a character limit")
                        .buildOption();
                });

                cat0MidLong = cat0.addLongOption("cat0MidLong")
                    .setDefault(5000L)
                    .setRange(100L, 1000000L)
                    .addComment("Some ranged long value")
                    .buildOption();
                cat0MidFloat = cat0.addFloatOption("cat0MidFloat")
                    .setDefault(1.0f)
                    .setRange(0f, 5f)
                    .addComment("Some ranged float value")
                    .buildOption();
                cat0MidDouble = cat0.addDoubleOption("cat0MidDouble")
                    .setDefault(0.25)
                    .setRange(0.0, 1.0)
                    .addComment("Some ranged double value")
                    .buildOption();

                cat0.addCategory("Cat-in-cat 2", cat2 -> {
                    cat2Bool = cat2.addBooleanOption("cat2Bool")
                        .setDefault(true)
                        .addComment("Some boolean value with a flag")
                        .addFlags(OptionFlags.REQUIRES_GAME_RESTART)
                        .buildOption();
                    cat2Int = cat2.addIntOption("cat2Int")
                        .setDefault(-5)
                        .setRange(-10, -1)
                        .addComment("Some negative ranged integer value")
                        .buildOption();
                    cat2Long = cat2.addLongOption("cat2Long")
                        .setDefault(-500L)
                        .setRange(-1000L, 0L)
                        .addComment("Some negative ranged long value")
                        .buildOption();
                    cat2Float = cat2.addFloatOption("cat2Float")
                        .setDefault(-1.5f)
                        .addComment("Some float value")
                        .buildOption();
                    cat2Double = cat2.addDoubleOption("cat2Double")
                        .setDefault(Mth.PI)
                        .addComment("Some double value")
                        .buildOption();
                    cat2String = cat2.addStringOption("cat2String")
                        .setDefault("")
                        .addComment("Some empty string value")
                        .buildOption();
                });

                cat0EndBool = cat0.addBooleanOption("cat0EndBool")
                    .setDefault(false)
                    .addComment("Some boolean value after every child category")
                    .buildOption();
            });
        }).build();
    }
}