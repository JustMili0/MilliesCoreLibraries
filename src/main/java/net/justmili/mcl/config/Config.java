package net.justmili.mcl.config;

import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v2.lib.config.*;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;

public class Config {
    public static final MConfigBuilder COMMON = new MConfigBuilder(CoreLibs.ID, null, ConfigTypes.COMMON, FileTypes.JSON5, true);
    public static ConfigOption<Boolean> enableDebug;
    public static ConfigOption<Boolean> enableExampleMode;

    public static void initCommon() {
        COMMON.create(root -> {

            enableDebug = root.addBoolOption("enableDebugLogging")
                .setDefault(false)
                .addComment("Enables additional debug information for actions executed by Millie's Core Libraries")
                .buildOption();

            enableExampleMode = root.addBoolOption("enableExampleMode")
                .setDefault(false)
                .addComment("Enables generation of \"examplemod\" config files. Used for testing.")
                .buildOption();

        }).build();
    }
}