package net.justmili.mcl.config;

import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v2.lib.config.*;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;
import net.minecraft.network.chat.Component;

public class Config {
    public static final MConfigBuilder common = new MConfigBuilder(CoreLibs.ID, null, ConfigTypes.COMMON, FileTypes.JSON5, true);
    public static ConfigOption<Boolean> enableDebug;

    public static void initCommon() {
        common.create(root -> {

            enableDebug = root.addBoolOption("enableDebugLogging")
                .setDefault(false)
                .addComment(Component.translatable("corelibs.config.comment.debug"))
                .buildOption();

        }).build();
    }
}
