package net.justmili.mcl.config;

import com.mojang.serialization.Codec;
import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v2.lib.codecs.ColorCodec;
import net.justmili.mcl.v2.lib.codecs.EnumCodec;
import net.justmili.mcl.v2.lib.config.*;
import net.justmili.mcl.v2.lib.config.options.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.awt.*;

public class ExampleConfig {
    public static ConfigOption<Boolean> bool1, bool2, bool3, bool4;
    public static ConfigOption<String> string1, string2, string3, string4, stringLimited1, stringLimited2, stringLimited3, stringLimited4;
    public static ConfigOption<Integer> int1, int2, int3, int4, intRanged1, intRanged2, intRanged3, intRanged4, intRangedNeg1, intRangedNeg2, intRangedNeg3, intRangedNeg4;
    public static ConfigOption<Long> long1, long2, long3, long4, longRanged1, longRanged2, longRanged3, longRanged4, longRangedNeg1, longRangedNeg2, longRangedNeg3, longRangedNeg4;
    public static ConfigOption<Float> float1, float2, float3, float4, floatRanged1, floatRanged2, floatRanged3, floatRanged4, floatRangedNeg1, floatRangedNeg2, floatRangedNeg3, floatRangedNeg4;
    public static ConfigOption<Double> double1, double2, double3, double4, doubleRanged1, doubleRanged2, doubleRanged3, doubleRanged4, doubleRangedNeg1, doubleRangedNeg2, doubleRangedNeg3, doubleRangedNeg4;
    public static ConfigOption<TestConfigEnum> enum1, enum2, enum3, enum4;
    public static ConfigOption<Color> rgb1, rgb2, rgb3, rgb4, argb1, argb2, argb3, argb4;
    public static ListConfigOption<String> stringList1, stringList2, stringList3, stringList4;
    public static ListConfigOption<Integer> intList1, intList2, intList3, intList4;
    public static ListConfigOption<Long> longList1, longList2, longList3, longList4;
    public static ListConfigOption<Float> floatList1, floatList2, floatList3, floatList4;
    public static ListConfigOption<Double> doubleList1, doubleList2, doubleList3, doubleList4;
    public static ListConfigOption<TestConfigEnum> enumList1, enumList2, enumList3, enumList4;
    public static ListConfigOption<Color> rgbList1, rgbList2, rgbList3, rgbList4;
    public static HolderSetConfigOption<Block> blockSet1, blockSet2, blockSet3, blockSet4;
    public static HolderSetConfigOption<Item> itemSet1, itemSet2, itemSet3, itemSet4;
    public static HolderSetConfigOption<EntityType<?>> entitySet1, entitySet2, entitySet3, entitySet4;

    public static void initCommon() {
        // Return and don't init anything if exampleMode is off
        if (!Config.enableExampleMode.get()) return;

        // Init config files for each file type
        for (var type : FileTypes.getEntries()) {
            if (type == FileTypes.TOML) continue; // TOML isn't finished
            buildWith(builderOfType(type));
        }

        // Log options for debug
        /* for (var option : new ConfigOption<?>[]{
            bool4, string4, stringLimited4,
            int4, intRanged4, intRangedNeg4,
            long4, longRanged4, longRangedNeg4,
            float4, floatRanged4, floatRangedNeg4,
            double4, doubleRanged4, doubleRangedNeg4,
            enum4, rgb4, argb4,
            stringList4, intList4, longList4, floatList4, doubleList4, enumList4, rgbList4,
            blockSet4, itemSet4, entitySet4
        }) {
            CoreLibs.LOGGER.info("{}", option);
        }*/
    }

    // Functions made only for purpose of this example
    private static MConfigBuilder builderOfType(FileTypes ext) {
        return new MConfigBuilder("examplemod", null, ConfigTypes.COMMON, ext, true);
    }

    private static void buildWith(MConfigBuilder builder) {
        builder.create(root -> {
            root.addComment("1st root comment");
            root.addComment("2nd root comment");
            root.addComment("3rd root comment\nwith two lines");
            root.addComment("4th root comment\nwith three\nlines");
            root.addComment("Stacked root comment").addComment("mmmmm").addComment("monkey");
            root.addComment("""
                Text block root comment
                Hello world
                Henlo everynyan
                """);

            root.addCategory("Category C", d1 -> {
                enumList2 = d1.addListOption("enumList2", EnumCodec.of(TestConfigEnum.class), false)
                    .setDefault(TestConfigEnum.VALUE_0, TestConfigEnum.VALUE_1, TestConfigEnum.VALUE_2)
                    .addComment("Comment of enumList2")
                    .buildOption();

                longRanged4 = d1.addLongOption("longRanged4")
                    .setDefault(8192L)
                    .setRange(0L, 10000L)
                    .addComment("Comment of longRanged4 1")
                    .addComment("Comment of longRanged4 2")
                    .addFlags(OptionFlags.REQUIRES_GAME_RESTART)
                    .buildOption();

                floatList1 = d1.addListOption("floatList1", Codec.FLOAT, true)
                    .setDefault(0.5f, 0.5f, 1.5f)
                    .buildOption();

                argb2 = d1.addArgbColorOption("argb2")
                    .setDefault(0xFF4B0082)
                    .addComment("Comment of argb2")
                    .buildOption();

                rgbList2 = d1.addListOption("rgbList2", ColorCodec.RGB_HEX, false)
                    .setDefault(new Color(0xFF0000), new Color(0xFF7F00), new Color(0xFFFF00))
                    .addComment("Comment of rgbList2")
                    .buildOption();

                entitySet1 = d1.addHolderSetOption("entitySet1", BuiltInRegistries.ENTITY_TYPE)
                    .addDefault(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.ENDERMAN, EntityType.SPIDER)
                    .addDefault(EntityTypeTags.SKELETONS)
                    .buildOption();

                floatRanged1 = d1.addFloatOption("floatRanged1")
                    .setDefault(1.0f)
                    .setRange(0.0f, 10.0f)
                    .buildOption();

                longRangedNeg3 = d1.addLongOption("longRangedNeg3")
                    .setDefault(-27L)
                    .setRange(-100L, -1L)
                    .addFlags(OptionFlags.REQUIRES_RENDERER_RELOAD, OptionFlags.REQUIRES_LEVEL_RELOAD)
                    .buildOption();

                int3 = d1.addIntOption("int3")
                    .setDefault(8)
                    .addFlags(OptionFlags.REQUIRES_DATAPACK_RELOAD)
                    .buildOption();

                longList1 = d1.addListOption("longList1", Codec.LONG, true)
                    .setDefault(1L, 2L, 2L, 3L, 3L, 3L)
                    .buildOption();

                d1.addCategory("Cat-in-cat C1", d2 -> {
                    rgb3 = d2.addRgbColorOption("rgb3")
                        .setDefault(1f, 1f, 0f)
                        .addFlags(OptionFlags.REQUIRES_VIDEOMODE_RELOAD, OptionFlags.REQUIRES_ASSETS_RELOAD)
                        .buildOption();

                    floatList2 = d2.addListOption("floatList2", Codec.FLOAT, false)
                        .setDefault(0.25f, 0.5f, 1.0f)
                        .addComment("Comment of floatList2")
                        .buildOption();

                    double1 = d2.addDoubleOption("double1")
                        .setDefault(0.1)
                        .buildOption();

                    rgb4 = d2.addRgbColorOption("rgb4")
                        .setDefault("00FF00")
                        .addComment("Comment of rgb4 1")
                        .addComment("Comment of rgb4 2")
                        .addComment("Comment of rgb4 3")
                        .addFlags(OptionFlags.REQUIRES_RENDERER_UPDATE)
                        .buildOption();

                    doubleRangedNeg1 = d2.addDoubleOption("doubleRangedNeg1")
                        .setDefault(-0.5)
                        .setRange(-5.0, 0.0)
                        .buildOption();

                    itemSet4 = d2.addHolderSetOption("itemSet4", BuiltInRegistries.ITEM)
                        .addDefault(Items.BREAD)
                        .addDefault(ItemTags.PLANKS)
                        .addDefault("minecraft:coal")
                        .addComment("Comment of itemSet4")
                        .addFlags(OptionFlags.REQUIRES_GAME_RESTART, OptionFlags.REQUIRES_RENDERER_RELOAD)
                        .buildOption();

                    longRangedNeg4 = d2.addLongOption("longRangedNeg4")
                        .setDefault(-81L)
                        .setRange(-100L, -1L)
                        .addComment("Comment of longRangedNeg4 1")
                        .addComment("Comment of longRangedNeg4 2")
                        .addFlags(OptionFlags.REQUIRES_LEVEL_RELOAD)
                        .buildOption();

                    blockSet1 = d2.addHolderSetOption("blockSet1", BuiltInRegistries.BLOCK)
                        .addDefault(Blocks.OAK_LOG, Blocks.SPRUCE_LOG)
                        .buildOption();
                });
            }).addComment("Comment of Category C");

            intRangedNeg2 = root.addIntOption("intRangedNeg2")
                .setDefault(-100)
                .setRange(-1000, -1)
                .addComment("Comment of intRangedNeg2")
                .buildOption();

            root.addCategory("Empty Category", d1 -> {
            }).addComment("Comment of Empty Category");

            long2 = root.addLongOption("long2")
                .setDefault(100L)
                .addComment("Comment of long2")
                .buildOption();

            root.addCategory("Category A", d1 -> {
                int1 = d1.addIntOption("int1")
                    .setDefault(2)
                    .buildOption();

                intList2 = d1.addListOption("intList2", Codec.INT, false)
                    .setDefault(1, 2, 4, 8, 16)
                    .addComment("Comment of intList2")
                    .buildOption();

                d1.addCategory("Cat-in-cat A2", d2 -> {
                    entitySet3 = d2.addHolderSetOption("entitySet3", BuiltInRegistries.ENTITY_TYPE)
                        .addDefault("minecraft:creeper", "minecraft:spider")
                        .addFlags(OptionFlags.REQUIRES_DATAPACK_RELOAD, OptionFlags.REQUIRES_VIDEOMODE_RELOAD)
                        .buildOption();

                    stringList4 = d2.addListOption("stringList4", Codec.STRING, true)
                        .setDefault("foo", "bar", "baz", "foo")
                        .addComment("Comment of stringList4 1")
                        .addComment("Comment of stringList4 2")
                        .addComment("Comment of stringList4 3")
                        .addFlags(OptionFlags.REQUIRES_ASSETS_RELOAD)
                        .buildOption();

                    int2 = d2.addIntOption("int2")
                        .setDefault(4)
                        .addComment("Comment of int2")
                        .buildOption();

                    blockSet2 = d2.addHolderSetOption("blockSet2", BuiltInRegistries.BLOCK)
                        .addDefault(BlockTags.LOGS, BlockTags.LOGS_THAT_BURN)
                        .addComment("Comment of blockSet2")
                        .buildOption();

                    long4 = d2.addLongOption("long4")
                        .setDefault(10000L)
                        .addComment("Comment of long4")
                        .addFlags(OptionFlags.REQUIRES_RENDERER_UPDATE, OptionFlags.REQUIRES_GAME_RESTART)
                        .buildOption();

                    intRangedNeg1 = d2.addIntOption("intRangedNeg1")
                        .setDefault(-10)
                        .setRange(-100, -1)
                        .buildOption();

                    argb4 = d2.addArgbColorOption("argb4")
                        .setDefault("80FF0000")
                        .addComment("Comment of argb4 1")
                        .addComment("Comment of argb4 2")
                        .addFlags(OptionFlags.REQUIRES_RENDERER_RELOAD)
                        .buildOption();

                    intRanged1 = d2.addIntOption("intRanged1")
                        .setDefault(3)
                        .setRange(0, 100)
                        .buildOption();

                    float3 = d2.addFloatOption("float3")
                        .setDefault(0.125f)
                        .addFlags(OptionFlags.REQUIRES_LEVEL_RELOAD, OptionFlags.REQUIRES_DATAPACK_RELOAD)
                        .buildOption();

                    doubleRanged2 = d2.addDoubleOption("doubleRanged2")
                        .setDefault(1.5)
                        .setRange(0.0, 5.0)
                        .addComment("Comment of doubleRanged2")
                        .buildOption();

                    d2.addCategory("Cat-in-cat-in-cat A3", d3 -> {
                        d3.addCategory("Cat x4 A4", d4 -> {
                            rgbList3 = d4.addListOption("rgbList3", ColorCodec.RGB_HEX, false)
                                .setDefault(new Color(0x00FFFF), new Color(0xFF00FF), new Color(0x000000))
                                .addFlags(OptionFlags.REQUIRES_VIDEOMODE_RELOAD)
                                .buildOption();

                            d4.addCategory("Cat x5 A5", d5 -> {
                                argb3 = d5.addArgbColorOption("argb3")
                                    .setDefault(1f, 0.56f, 0f, 1f)
                                    .addFlags(OptionFlags.REQUIRES_ASSETS_RELOAD, OptionFlags.REQUIRES_RENDERER_UPDATE)
                                    .buildOption();

                                long1 = d5.addLongOption("long1")
                                    .setDefault(10L)
                                    .buildOption();

                                floatRanged4 = d5.addFloatOption("floatRanged4")
                                    .setDefault(4.0f)
                                    .setRange(0.0f, 10.0f)
                                    .addComment("Comment of floatRanged4 1")
                                    .addComment("Comment of floatRanged4 2")
                                    .addComment("Comment of floatRanged4 3")
                                    .addFlags(OptionFlags.REQUIRES_GAME_RESTART)
                                    .buildOption();

                                stringLimited1 = d5.addStringOption("stringLimited1")
                                    .setDefault("Hello world")
                                    .setCharLimit(16)
                                    .buildOption();

                                floatRangedNeg4 = d5.addFloatOption("floatRangedNeg4")
                                    .setDefault(-4.0f)
                                    .setRange(-10.0f, -1.0f)
                                    .addComment("Comment of floatRangedNeg4 1")
                                    .addComment("Comment of floatRangedNeg4 2")
                                    .addFlags(OptionFlags.REQUIRES_RENDERER_RELOAD, OptionFlags.REQUIRES_LEVEL_RELOAD)
                                    .buildOption();

                                stringLimited3 = d5.addStringOption("stringLimited3")
                                    .setDefault("Foo bar")
                                    .setCharLimit(16)
                                    .addFlags(OptionFlags.REQUIRES_DATAPACK_RELOAD)
                                    .buildOption();
                            });

                            itemSet2 = d4.addHolderSetOption("itemSet2", BuiltInRegistries.ITEM)
                                .addDefault(ItemTags.PLANKS)
                                .addComment("Comment of itemSet2")
                                .buildOption();

                            rgb1 = d4.addRgbColorOption("rgb1")
                                .setDefault("FF0000")
                                .buildOption();

                            doubleList4 = d4.addListOption("doubleList4", Codec.DOUBLE, true)
                                .setDefault(0.1, 0.01, 0.1)
                                .addComment("Comment of doubleList4 1")
                                .addComment("Comment of doubleList4 2")
                                .addComment("Comment of doubleList4 3")
                                .addComment("Comment of doubleList4 4")
                                .addFlags(OptionFlags.REQUIRES_VIDEOMODE_RELOAD, OptionFlags.REQUIRES_ASSETS_RELOAD)
                                .buildOption();

                            itemSet3 = d4.addHolderSetOption("itemSet3", BuiltInRegistries.ITEM)
                                .addDefault("minecraft:apple", "#minecraft:swords")
                                .addFlags(OptionFlags.REQUIRES_RENDERER_UPDATE)
                                .buildOption();

                            longList3 = d4.addListOption("longList3", Codec.LONG, false)
                                .setDefault(1L, 3L, 9L, 27L, 81L)
                                .addFlags(OptionFlags.REQUIRES_GAME_RESTART, OptionFlags.REQUIRES_RENDERER_RELOAD)
                                .buildOption();

                            doubleRanged4 = d4.addDoubleOption("doubleRanged4")
                                .setDefault(3.5)
                                .setRange(0.0, 5.0)
                                .addComment("Comment of doubleRanged4 1")
                                .addComment("Comment of doubleRanged4 2")
                                .addFlags(OptionFlags.REQUIRES_LEVEL_RELOAD)
                                .buildOption();

                            floatRanged2 = d4.addFloatOption("floatRanged2")
                                .setDefault(2.0f)
                                .setRange(0.0f, 10.0f)
                                .addComment("Comment of floatRanged2")
                                .buildOption();

                            bool3 = d4.addBoolOption("bool3")
                                .setDefault(true)
                                .addFlags(OptionFlags.REQUIRES_DATAPACK_RELOAD, OptionFlags.REQUIRES_VIDEOMODE_RELOAD)
                                .buildOption();

                            stringLimited2 = d4.addStringOption("stringLimited2")
                                .setDefault("Lorem ipsum")
                                .setCharLimit(16)
                                .addComment("Comment of stringLimited2")
                                .buildOption();

                            rgbList4 = d4.addListOption("rgbList4", ColorCodec.RGB_HEX, true)
                                .setDefault(new Color(0xFFFFFF), new Color(0x000000), new Color(0xFFFFFF))
                                .addComment("Comment of rgbList4 1")
                                .addComment("Comment of rgbList4 2")
                                .addFlags(OptionFlags.REQUIRES_ASSETS_RELOAD)
                                .buildOption();

                            float2 = d4.addFloatOption("float2")
                                .setDefault(0.25f)
                                .addComment("Comment of float2")
                                .buildOption();

                            enum1 = d4.addEnumOption("enum1", TestConfigEnum.class)
                                .setDefault(TestConfigEnum.VALUE_0)
                                .buildOption();
                        }).addComment("Comment of Cat x4 A4 1").addComment("Comment of Cat x4 A4 2");

                        stringList1 = d3.addListOption("stringList1", Codec.STRING, true)
                            .setDefault("a", "b", "a", "c")
                            .buildOption();

                        intList3 = d3.addListOption("intList3", Codec.INT, false)
                            .setDefault(1, 3, 9, 27, 81)
                            .addFlags(OptionFlags.REQUIRES_RENDERER_UPDATE, OptionFlags.REQUIRES_GAME_RESTART)
                            .buildOption();

                        doubleRanged1 = d3.addDoubleOption("doubleRanged1")
                            .setDefault(0.5)
                            .setRange(0.0, 5.0)
                            .buildOption();

                        string1 = d3.addStringOption("string1")
                            .setDefault("Hello world")
                            .buildOption();

                        long3 = d3.addLongOption("long3")
                            .setDefault(1000L)
                            .addFlags(OptionFlags.REQUIRES_RENDERER_RELOAD)
                            .buildOption();

                        intRanged4 = d3.addIntOption("intRanged4")
                            .setDefault(81)
                            .setRange(0, 100)
                            .addComment("Comment of intRanged4")
                            .addFlags(OptionFlags.REQUIRES_LEVEL_RELOAD, OptionFlags.REQUIRES_DATAPACK_RELOAD)
                            .buildOption();

                        doubleRangedNeg2 = d3.addDoubleOption("doubleRangedNeg2")
                            .setDefault(-1.5)
                            .setRange(-5.0, 0.0)
                            .addComment("Comment of doubleRangedNeg2")
                            .buildOption();

                        bool2 = d3.addBoolOption("bool2")
                            .setDefault(false)
                            .addComment("Comment of bool2")
                            .buildOption();

                        intList1 = d3.addListOption("intList1", Codec.INT, true)
                            .setDefault(1, 2, 2, 3, 3, 3)
                            .buildOption();

                        intRanged3 = d3.addIntOption("intRanged3")
                            .setDefault(27)
                            .setRange(0, 100)
                            .addFlags(OptionFlags.REQUIRES_VIDEOMODE_RELOAD)
                            .buildOption();
                    });

                    string3 = d2.addStringOption("string3")
                        .setDefault("Foo bar")
                        .addFlags(OptionFlags.REQUIRES_ASSETS_RELOAD, OptionFlags.REQUIRES_RENDERER_UPDATE)
                        .buildOption();
                }).addComment("Comment of Cat-in-cat A2 1").addComment("Comment of Cat-in-cat A2 2").addComment("Comment of Cat-in-cat A2 3");

                floatList4 = d1.addListOption("floatList4", Codec.FLOAT, true)
                    .setDefault(0.1f, 0.01f, 0.1f)
                    .addComment("Comment of floatList4 1")
                    .addComment("Comment of floatList4 2")
                    .addFlags(OptionFlags.REQUIRES_GAME_RESTART)
                    .buildOption();

                doubleRangedNeg4 = d1.addDoubleOption("doubleRangedNeg4")
                    .setDefault(-3.5)
                    .setRange(-5.0, 0.0)
                    .addComment("Comment of doubleRangedNeg4 1")
                    .addComment("Comment of doubleRangedNeg4 2")
                    .addComment("Comment of doubleRangedNeg4 3")
                    .addFlags(OptionFlags.REQUIRES_RENDERER_RELOAD, OptionFlags.REQUIRES_LEVEL_RELOAD)
                    .buildOption();

                enum2 = d1.addEnumOption("enum2", TestConfigEnum.class)
                    .setDefault(TestConfigEnum.VALUE_1)
                    .addComment("Comment of enum2")
                    .buildOption();

                enum4 = d1.addEnumOption("enum4", TestConfigEnum.class)
                    .setDefault(TestConfigEnum.VALUE_3)
                    .addComment("Comment of enum4 1")
                    .addComment("Comment of enum4 2")
                    .addFlags(OptionFlags.REQUIRES_DATAPACK_RELOAD)
                    .buildOption();

                enumList1 = d1.addListOption("enumList1", EnumCodec.of(TestConfigEnum.class), true)
                    .setDefault(TestConfigEnum.VALUE_0, TestConfigEnum.VALUE_1, TestConfigEnum.VALUE_1)
                    .buildOption();

                doubleList1 = d1.addListOption("doubleList1", Codec.DOUBLE, true)
                    .setDefault(0.5, 0.5, 1.5)
                    .buildOption();

                d1.addCategory("Cat-in-cat A1", d2 -> {
                    stringList3 = d2.addListOption("stringList3", Codec.STRING, false)
                        .setDefault("one", "two", "three")
                        .addFlags(OptionFlags.REQUIRES_VIDEOMODE_RELOAD, OptionFlags.REQUIRES_ASSETS_RELOAD)
                        .buildOption();

                    int4 = d2.addIntOption("int4")
                        .setDefault(16)
                        .addComment("Comment of int4 1")
                        .addComment("Comment of int4 2")
                        .addFlags(OptionFlags.REQUIRES_RENDERER_UPDATE)
                        .buildOption();

                    double3 = d2.addDoubleOption("double3")
                        .setDefault(0.001)
                        .addFlags(OptionFlags.REQUIRES_GAME_RESTART, OptionFlags.REQUIRES_RENDERER_RELOAD)
                        .buildOption();

                    stringLimited4 = d2.addStringOption("stringLimited4")
                        .setDefault("abc")
                        .setCharLimit(16)
                        .addComment("Comment of stringLimited4 1")
                        .addComment("Comment of stringLimited4 2")
                        .addComment("Comment of stringLimited4 3")
                        .addFlags(OptionFlags.REQUIRES_LEVEL_RELOAD)
                        .buildOption();
                });
            });

            enumList3 = root.addListOption("enumList3", EnumCodec.of(TestConfigEnum.class), false)
                .setDefault(TestConfigEnum.VALUE_4, TestConfigEnum.VALUE_3, TestConfigEnum.VALUE_2)
                .addFlags(OptionFlags.REQUIRES_DATAPACK_RELOAD, OptionFlags.REQUIRES_VIDEOMODE_RELOAD)
                .buildOption();

            longRangedNeg1 = root.addLongOption("longRangedNeg1")
                .setDefault(-3L)
                .setRange(-100L, -1L)
                .buildOption();

            longRanged2 = root.addLongOption("longRanged2")
                .setDefault(2048L)
                .setRange(0L, 10000L)
                .addComment("Comment of longRanged2")
                .buildOption();

            longRanged1 = root.addLongOption("longRanged1")
                .setDefault(1024L)
                .setRange(0L, 10000L)
                .buildOption();

            floatRangedNeg1 = root.addFloatOption("floatRangedNeg1")
                .setDefault(-1.0f)
                .setRange(-10.0f, -1.0f)
                .buildOption();

            blockSet4 = root.addHolderSetOption("blockSet4", BuiltInRegistries.BLOCK)
                .addDefault(Blocks.STONE)
                .addDefault(BlockTags.BASE_STONE_OVERWORLD)
                .addDefault("minecraft:dirt")
                .addComment("Comment of blockSet4 1")
                .addComment("Comment of blockSet4 2")
                .addFlags(OptionFlags.REQUIRES_ASSETS_RELOAD)
                .buildOption();

            longRangedNeg2 = root.addLongOption("longRangedNeg2")
                .setDefault(-9L)
                .setRange(-100L, -1L)
                .addComment("Comment of longRangedNeg2")
                .buildOption();

            root.addCategory("Category B", d1 -> {
                longRanged3 = d1.addLongOption("longRanged3")
                    .setDefault(4096L)
                    .setRange(0L, 10000L)
                    .addFlags(OptionFlags.REQUIRES_RENDERER_UPDATE, OptionFlags.REQUIRES_GAME_RESTART)
                    .buildOption();

                intList4 = d1.addListOption("intList4", Codec.INT, true)
                    .setDefault(10, 100, 100, 1000)
                    .addComment("Comment of intList4 1")
                    .addComment("Comment of intList4 2")
                    .addFlags(OptionFlags.REQUIRES_RENDERER_RELOAD)
                    .buildOption();

                blockSet3 = d1.addHolderSetOption("blockSet3", BuiltInRegistries.BLOCK)
                    .addDefault("minecraft:oak_planks", "#minecraft:planks")
                    .addFlags(OptionFlags.REQUIRES_LEVEL_RELOAD, OptionFlags.REQUIRES_DATAPACK_RELOAD)
                    .buildOption();

                argb1 = d1.addArgbColorOption("argb1")
                    .setDefault("FF0000FF")
                    .buildOption();

                longList2 = d1.addListOption("longList2", Codec.LONG, false)
                    .setDefault(1L, 2L, 4L, 8L, 16L)
                    .addComment("Comment of longList2")
                    .buildOption();

                rgb2 = d1.addRgbColorOption("rgb2")
                    .setDefault(0xFF7F00)
                    .addComment("Comment of rgb2")
                    .buildOption();

                doubleList3 = d1.addListOption("doubleList3", Codec.DOUBLE, false)
                    .setDefault(1.0, 2.0, 3.0)
                    .addFlags(OptionFlags.REQUIRES_VIDEOMODE_RELOAD)
                    .buildOption();

                rgbList1 = d1.addListOption("rgbList1", ColorCodec.RGB_HEX, true)
                    .setDefault(new Color(0xFF0000), new Color(0x00FF00), new Color(0x0000FF))
                    .buildOption();

                d1.addCategory("Cat-in-cat B1", d2 -> {
                    longList4 = d2.addListOption("longList4", Codec.LONG, true)
                        .setDefault(10L, 100L, 100L, 1000L)
                        .addComment("Comment of longList4 1")
                        .addComment("Comment of longList4 2")
                        .addComment("Comment of longList4 3")
                        .addComment("Comment of longList4 4")
                        .addFlags(OptionFlags.REQUIRES_ASSETS_RELOAD, OptionFlags.REQUIRES_RENDERER_UPDATE)
                        .buildOption();

                    doubleRanged3 = d2.addDoubleOption("doubleRanged3")
                        .setDefault(2.5)
                        .setRange(0.0, 5.0)
                        .addFlags(OptionFlags.REQUIRES_GAME_RESTART)
                        .buildOption();

                    itemSet1 = d2.addHolderSetOption("itemSet1", BuiltInRegistries.ITEM)
                        .addDefault(Items.DIAMOND, Items.STICK)
                        .buildOption();

                    floatRangedNeg2 = d2.addFloatOption("floatRangedNeg2")
                        .setDefault(-2.0f)
                        .setRange(-10.0f, -1.0f)
                        .addComment("Comment of floatRangedNeg2")
                        .buildOption();

                    d2.addCategory("Cat-in-cat-in-cat B2", d3 -> {
                        floatList3 = d3.addListOption("floatList3", Codec.FLOAT, false)
                            .setDefault(1.0f, 2.0f, 3.0f)
                            .addFlags(OptionFlags.REQUIRES_RENDERER_RELOAD, OptionFlags.REQUIRES_LEVEL_RELOAD)
                            .buildOption();

                        floatRanged3 = d3.addFloatOption("floatRanged3")
                            .setDefault(3.0f)
                            .setRange(0.0f, 10.0f)
                            .addFlags(OptionFlags.REQUIRES_DATAPACK_RELOAD)
                            .buildOption();

                        double2 = d3.addDoubleOption("double2")
                            .setDefault(0.01)
                            .addComment("Comment of double2")
                            .buildOption();

                        bool1 = d3.addBoolOption("bool1")
                            .setDefault(true)
                            .buildOption();

                        string4 = d3.addStringOption("string4")
                            .setDefault("")
                            .addComment("Comment of string4 1")
                            .addComment("Comment of string4 2")
                            .addFlags(OptionFlags.REQUIRES_VIDEOMODE_RELOAD, OptionFlags.REQUIRES_ASSETS_RELOAD)
                            .buildOption();

                        entitySet4 = d3.addHolderSetOption("entitySet4", BuiltInRegistries.ENTITY_TYPE)
                            .addDefault(EntityType.PIG)
                            .addDefault("#minecraft:skeletons")
                            .addComment("Comment of entitySet4 1")
                            .addComment("Comment of entitySet4 2")
                            .addFlags(OptionFlags.REQUIRES_RENDERER_UPDATE)
                            .buildOption();

                        bool4 = d3.addBoolOption("bool4")
                            .setDefault(false)
                            .addComment("Comment of bool4 1")
                            .addComment("Comment of bool4 2")
                            .addComment("Comment of bool4 3")
                            .addFlags(OptionFlags.REQUIRES_GAME_RESTART, OptionFlags.REQUIRES_RENDERER_RELOAD)
                            .buildOption();

                        float1 = d3.addFloatOption("float1")
                            .setDefault(0.5f)
                            .buildOption();

                        float4 = d3.addFloatOption("float4")
                            .setDefault(0.0625f)
                            .addComment("Comment of float4 1")
                            .addComment("Comment of float4 2")
                            .addFlags(OptionFlags.REQUIRES_LEVEL_RELOAD)
                            .buildOption();

                        intRangedNeg3 = d3.addIntOption("intRangedNeg3")
                            .setDefault(-1000)
                            .setRange(-10000, -1)
                            .addFlags(OptionFlags.REQUIRES_DATAPACK_RELOAD, OptionFlags.REQUIRES_VIDEOMODE_RELOAD)
                            .buildOption();

                        intRangedNeg4 = d3.addIntOption("intRangedNeg4")
                            .setDefault(-10000)
                            .setRange(-100000, -1)
                            .addComment("Comment of intRangedNeg4 1")
                            .addComment("Comment of intRangedNeg4 2")
                            .addFlags(OptionFlags.REQUIRES_ASSETS_RELOAD)
                            .buildOption();

                        intRanged2 = d3.addIntOption("intRanged2")
                            .setDefault(9)
                            .setRange(0, 100)
                            .addComment("Comment of intRanged2")
                            .buildOption();
                    });

                    enum3 = d2.addEnumOption("enum3", TestConfigEnum.class)
                        .setDefault(TestConfigEnum.VALUE_2)
                        .addFlags(OptionFlags.REQUIRES_RENDERER_UPDATE, OptionFlags.REQUIRES_GAME_RESTART)
                        .buildOption();

                    entitySet2 = d2.addHolderSetOption("entitySet2", BuiltInRegistries.ENTITY_TYPE)
                        .addDefault("#minecraft:raiders")
                        .addComment("Comment of entitySet2")
                        .buildOption();

                    floatRangedNeg3 = d2.addFloatOption("floatRangedNeg3")
                        .setDefault(-3.0f)
                        .setRange(-10.0f, -1.0f)
                        .addFlags(OptionFlags.REQUIRES_RENDERER_RELOAD)
                        .buildOption();

                    enumList4 = d2.addListOption("enumList4", EnumCodec.of(TestConfigEnum.class), true)
                        .setDefault(TestConfigEnum.VALUE_0, TestConfigEnum.VALUE_0, TestConfigEnum.VALUE_4)
                        .addComment("Comment of enumList4 1")
                        .addComment("Comment of enumList4 2")
                        .addFlags(OptionFlags.REQUIRES_LEVEL_RELOAD, OptionFlags.REQUIRES_DATAPACK_RELOAD)
                        .buildOption();
                });

                stringList2 = d1.addListOption("stringList2", Codec.STRING, false)
                    .setDefault("red", "orange", "yellow")
                    .addComment("Comment of stringList2")
                    .buildOption();

                double4 = d1.addDoubleOption("double4")
                    .setDefault(0.0001)
                    .addComment("Comment of double4 1")
                    .addComment("Comment of double4 2")
                    .addComment("Comment of double4 3")
                    .addFlags(OptionFlags.REQUIRES_VIDEOMODE_RELOAD)
                    .buildOption();
            });

            doubleList2 = root.addListOption("doubleList2", Codec.DOUBLE, false)
                .setDefault(0.25, 0.5, 1.0)
                .addComment("Comment of doubleList2")
                .buildOption();

            doubleRangedNeg3 = root.addDoubleOption("doubleRangedNeg3")
                .setDefault(-2.5)
                .setRange(-5.0, 0.0)
                .addFlags(OptionFlags.REQUIRES_ASSETS_RELOAD, OptionFlags.REQUIRES_RENDERER_UPDATE)
                .buildOption();

            string2 = root.addStringOption("string2")
                .setDefault("Lorem ipsum")
                .addComment("Comment of string2")
                .buildOption();
        }).build();
    }

    public enum TestConfigEnum {
        VALUE_0,
        VALUE_1,
        VALUE_2,
        VALUE_3,
        VALUE_4
    }
}