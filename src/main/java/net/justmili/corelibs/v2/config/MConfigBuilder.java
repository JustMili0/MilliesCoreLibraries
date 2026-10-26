package net.justmili.corelibs.v2.config;

import net.justmili.corelibs.v2.config.flags.ConfigTypes;
import net.justmili.corelibs.v2.config.flags.FileTypes;

public class MConfigBuilder {
    private final MConfigLoader loader;

    public MConfigBuilder(String modId, String customFileName, ConfigTypes configType, FileTypes fileType, boolean createSubDirectory) {
        this.loader = new MConfigLoader(modId, customFileName, configType, fileType, createSubDirectory);
    }

    public MConfigBuilder(String modId, ConfigTypes configType, FileTypes fileType, boolean createSubDirectory) {
        this.loader = new MConfigLoader(modId, null, configType, fileType, createSubDirectory);
    }

    public MConfigBuilder(String modId, String customFileName, ConfigTypes configType, FileTypes fileType) {
        this.loader = new MConfigLoader(modId, customFileName, configType, fileType, false);
    }

    public MConfigBuilder(String modId, ConfigTypes configType, FileTypes fileType) {
        this.loader = new MConfigLoader(modId, null, configType, fileType, false);
    }

    public void create() {
    }
}
