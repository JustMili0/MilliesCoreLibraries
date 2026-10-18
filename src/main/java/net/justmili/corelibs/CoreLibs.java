package net.justmili.corelibs;

import net.fabricmc.api.ModInitializer;
import net.justmili.corelibs.api.events.bridge.fabric.ServerEventsBridge;
import net.justmili.corelibs.config.sync.SyncCommonServerPriority;
import net.justmili.corelibs.core.config.ExampleConfig;
import net.justmili.corelibs.util.ModUtil;
import net.justmili.corelibs.util.common.ResourceUtil;
import net.justmili.corelibs.util.common.TickUtil;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreLibs implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(CoreLibs.class);
    public static final String ID = "corelibs";
    public static final String NAME = "Millie's Core Libraries";
    public static final String BUILD = "0.0.2a";

    @Override
    public void onInitialize() {
        ModUtil.specialInitMessage(LOGGER, NAME, ID, BUILD, ModUtil.VersionBuildType.EARLY_DEV_ALPHA);
        ModUtil.markEndOfSupport(NAME, ID, false, false);

        ServerEventsBridge.init();
        SyncCommonServerPriority.registerServer();

        TickUtil.registerProcessQueue();
        ExampleConfig.register();
    }

    public static ResourceLocation asId(String path) {
        return ResourceUtil.parse(ID, path);
    }
}