package net.justmili.corelibs;

import net.fabricmc.api.ModInitializer;
import net.justmili.corelibs.v1.api.events.bridge.ServerEventsBridge;
import net.justmili.corelibs.v2.config.sync.SyncPriorityConfigs;
import net.justmili.corelibs.v1.util.ModUtil;
import net.justmili.corelibs.v1.util.common.ResourceUtil;
import net.justmili.corelibs.v1.util.common.TickUtil;
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
        ModUtil.markEOS(NAME, ID, false, false);

        ServerEventsBridge.init();
        SyncPriorityConfigs.initS2C();

        TickUtil.registerProcessQueue();
    }

    public static ResourceLocation asId(String path) {
        return ResourceUtil.parse(ID, path);
    }
}