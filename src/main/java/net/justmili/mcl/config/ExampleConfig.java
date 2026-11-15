package net.justmili.mcl.config;

import com.mojang.serialization.Codec;
import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v2.lib.codecs.ColorCodec;
import net.justmili.mcl.v2.lib.config.*;
import net.justmili.mcl.v2.lib.config.options.*;
import net.justmili.mcl.v2.lib.util.common.Colors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.awt.*;

public class ExampleConfig {
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
    public static ConfigOption<TestConfigEnum> rootEnum;
    public static ConfigOption<Color> rgb;
    public static ConfigOption<Color> argb;
    public static ListConfigOption<String> cat2StringList;
    public static ListConfigOption<Color> cat1ColorList;
    public static HolderSetConfigOption<Block> rootHolderSet;

    public static void initCommon() {
        // Return and don't init anything if exampleMode is off
        if (!Config.enableExampleMode.get()) return;

        // Init config files for each file type
        for (var type : FileTypes.getEntries()) {
            if (type == FileTypes.TOML) continue; // TOML isn't finished
            buildWith(builderOfType(type));
        }
    }

    // Functions made only for purpose of this example
    private static MConfigBuilder builderOfType(FileTypes ext) {
        return new MConfigBuilder("examplemod", null, ConfigTypes.COMMON, ext, true);
    }

    private static void buildWith(MConfigBuilder builder) {
        builder.create(root -> {
            root.addComment("Config root comment");
            root.addComment("Second root comment\nwith two lines");

            /// RGB and ARGB values can be input as a HEX color string, an integer or float values
            rgb = root.addRgbColorOption("rootRGB")
                .setDefault("C95E35") // or 0xC95E35, or 0.7882f, 0.3686f, 0.2078f
                .addComment("Some RGB color value (HEX)")
                .buildOption();
            argb = root.addArgbColorOption("rootARGB")
                .setDefault(0xFFC95E35) // or "FFC95E35", or 1f, 0.7882f, 0.3686f, 0.2078f
                .addComment("Some ARGB color value (HEX)")
                .buildOption();

            /// For multiple type defaults, HolderSet options need each type be its own addDefault
            rootHolderSet = root.addHolderSetOption("rootHolderSet", BuiltInRegistries.BLOCK)
                .addDefault(Blocks.OAK_LOG, Blocks.SPRUCE_LOG)
                .addDefault(BlockTags.LOGS, BlockTags.LOGS_THAT_BURN)
                .addDefault("minecraft:oak_planks", "#minecraft:planks")
                .addFlags(OptionFlags.REQUIRES_DATAPACK_RELOAD) // This time it actually does need datapack reload
                .addComment("A HolderSet of registry IDs and tags")
                .buildOption();

            rootInt = root.addIntOption("rootInt")
                .setDefault(10)
                .addComment("Some integer value")
                .buildOption();

            rootIntRanged = root.addIntOption("rootIntRanged")
                .setDefault(10)
                .setRange(0, 99)
                .addComment("Value with flags")
                .addFlags(OptionFlags.REQUIRES_ASSETS_RELOAD, OptionFlags.REQUIRES_DATAPACK_RELOAD)
                .buildOption();

            rootIntRangedNeg = root.addIntOption("rootIntRangedNeg")
                .setDefault(-10)
                .setRange(-99, -1)
                .buildOption();

            rootEnum = root.addEnumOption("rootEnum", TestConfigEnum.class)
                .setDefault(TestConfigEnum.VALUE_2)
                .addComment("Some enum value")
                .buildOption();

            root.addCategory("Root Category", cat0 -> {

                cat0StartBool = cat0.addBoolOption("cat0StartBool")
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

                    /// Lists can be of anything, as long as it's of the same type
                    cat1ColorList = cat1.addListOption("cat1ColorList", ColorCodec.ARGB_HEX, true)
                        .setDefault(Colors.hexToColor("D73823C3", true), new Color(1f, 1f, 1f, 1f))
                        .addComment("Some colors list")
                        .buildOption();

                    cat1Bool = cat1.addBoolOption("cat1Bool")
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
                    .setDefault(1f)
                    .setRange(0f, 5f)
                    .addComment("Some ranged float value")
                    .buildOption();

                cat0MidDouble = cat0.addDoubleOption("cat0MidDouble")
                    .setDefault(0.25)
                    .setRange(0.0, 1.0)
                    .addComment("Some ranged double value")
                    .buildOption();

                cat0.addCategory("Cat-in-cat 2", cat2 -> {

                    cat2StringList = cat2.addListOption("cat2List", Codec.STRING, true)
                        .setDefault("a", "b", "a", "c", "c", "c")
                        .addComment("Some unique string list")
                        .buildOption();

                    cat2Bool = cat2.addBoolOption("cat2Bool")
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

                    cat2.addCategory("Cat-in-cat 3", cat3 -> {
                    }).addComment("Empty category");
                });

                cat0EndBool = cat0.addBoolOption("cat0EndBool")
                    .setDefault(false)
                    .addComment("Some boolean value after every child category")
                    .buildOption();
            });
        }).build();

        argb.set(Colors.floatToColor(0.6f, 0.4f, 0.4f, 0.4f));
        CoreLibs.LOGGER.info("{}@{}", builder, rootInt.toString());
        CoreLibs.LOGGER.info("{}@{}", builder, cat0StartString.toString());
        CoreLibs.LOGGER.info("{}@{}", builder, rootEnum.toString());
        CoreLibs.LOGGER.info("{}@{}", builder, argb.toString());
        CoreLibs.LOGGER.info("{}@{}", builder, cat2StringList.toString());
        CoreLibs.LOGGER.info("{}@{}", builder, cat1ColorList.toString());
        CoreLibs.LOGGER.info("{}@{}", builder, rootHolderSet.toString());
    }

    public enum TestConfigEnum {
        VALUE_0,
        VALUE_1,
        VALUE_2,
        VALUE_3,
        VALUE_4
    }
}