package net.justmili.mcl;

import net.fabricmc.api.ModInitializer;
import net.justmili.mcl.config.Config;
import net.justmili.mcl.config.ExampleConfig;
import net.justmili.mcl.v1.api.events.bridge.ServerEventsBridge;
import net.justmili.mcl.v1.lib.util.ModUtil;
import net.justmili.mcl.v1.lib.util.common.IdUtil;
import net.justmili.mcl.v1.lib.util.common.TickUtil;
import net.justmili.mcl.v2.lib.config.sync.ConfigSync;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreLibs implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(CoreLibs.class);
    public static final String ID = "corelibs";
    public static final String NAME = "Millie's Core Libraries";
    public static final String BUILD = "0.3.23b";

    @Override
    public void onInitialize() {
        ModUtil.specialInitMessage(LOGGER, NAME, ID, BUILD, ModUtil.VersionType.EARLY_DEV_BETA);

        Config.initCommon();
        ExampleConfig.initCommon();

        ServerEventsBridge.init();
        ConfigSync.initS2C();

        TickUtil.initProcessQueue();
    }

    public static ResourceLocation asId(String path) {
        return IdUtil.parse(ID, path);
    }
}