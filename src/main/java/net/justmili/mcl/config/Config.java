package net.justmili.mcl.config;

import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v2.lib.config.*;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;
import net.minecraft.network.chat.Component;

public class Config {
    public static final MConfigBuilder COMMON = new MConfigBuilder(CoreLibs.ID, null, ConfigTypes.COMMON, FileTypes.JSON5, true);
    public static ConfigOption<Boolean> enableDebug;
    public static ConfigOption<Boolean> enableExampleMode;

    public static void initCommon() {
        COMMON.create(root -> {

            enableDebug = root.addBoolOption("enableDebugLogging")
                .setDefault(false)
                .addComment(Component.translatable("corelibs.config.comment.debug"))
                .buildOption();

            enableExampleMode = root.addBoolOption("enableExampleMode")
                .setDefault(false)
                .addComment(Component.translatable("corelibs.config.comment.examplemode"))
                .buildOption();

        }).build();
    }
}
