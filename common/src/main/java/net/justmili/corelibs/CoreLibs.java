package net.justmili.corelibs;

import net.justmili.corelibs.config.ExampleConfig;
import net.justmili.util.utils.ModUtil;
import net.justmili.util.utils.common.ResourceUtil;
import net.justmili.util.utils.common.TickUtil;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreLibs {
    public static final Logger LOGGER = LoggerFactory.getLogger(CoreLibs.class);
    public static final String ID = "corelibs";
    public static final String NAME = "Millie's Core Libraries";
    public static final String BUILD = "";

    public static void init() {
        TickUtil.registerProcessQueue();

        ModUtil.specialInitMessage(LOGGER, NAME, ID, BUILD, ModUtil.VersionBuildType.EARLY_DEV_ALPHA);
        ModUtil.markEndOfSupport(NAME, ID, false, false);
        ExampleConfig.register();
    }

    public static ResourceLocation asId(String path) {
        return ResourceUtil.parse(ID, path);
    }
}