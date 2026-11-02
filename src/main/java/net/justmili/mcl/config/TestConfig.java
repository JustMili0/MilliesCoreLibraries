package net.justmili.mcl.config;

import net.justmili.mcl.v2.lib.config.*;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;
import net.justmili.mcl.v2.lib.config.options.OptionFlags;

public class TestConfig {
    public static ConfigOption<Integer> rootInt;
    public static ConfigOption<Integer> rootIntRanged;
    public static ConfigOption<Integer> rootIntRangedNeg;

    // Function made only for purpose of this example test
    private static MConfigBuilder createBuilder(FileTypes ext) {
        return new MConfigBuilder("examplemod", null, ConfigTypes.COMMON, ext, true);
    }

    public static void initCommon() {
        for (var type : FileTypes.getEntries()) {
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
                .addFlags(OptionFlags.REQUIRES_DUMMY_FLAG_1, OptionFlags.REQUIRES_DUMMY_FLAG_2)
                .buildOption();
            rootIntRangedNeg = root.addIntOption("rootIntRangedNeg")
                .setDefault(-10)
                .setRange(-99, -1)
                .buildOption();

            root.addCategory("Root Category", cat0 -> {

                // TODO: add some more values here
                // cat0Start[ValueType]..., something like that, similar for everything else

                cat0.addCategory("Cat-in-cat 1", cat1 -> {
                    // TODO: add some more values here
                });

                // TODO: add some more values here

                cat0.addCategory("Cat-in-cat 2", cat2 -> {
                    // TODO: add some more values here
                });
            });
        }).build();
    }
}
