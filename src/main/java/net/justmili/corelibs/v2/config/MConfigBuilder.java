package net.justmili.corelibs.v2.config;

import net.justmili.corelibs.v2.config.flags.ConfigTypes;
import net.justmili.corelibs.v2.config.flags.FileTypes;

import java.nio.file.Path;

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

    public ConfigBuilder create() {
        return new ConfigBuilder();
    }

    public static class ConfigBuilder {

        public ConfigBuilder() {
        }

        public void comment(String comment) {
        }

        public void addCategory(String name) {
        }

        public void addOption(String key, String value) {
        }

        public void addOption(String key, String value, int charLimit) {
        }

        public void addOption(String key, int value, int min, int max) {
        }

        public void addOption(String key, long value, long min, long max) {
        }

        public void addOption(String key, float value, float min, float max) {
        }

        public void addOption(String key, double value, double min, double max) {
        }

        public void addOption(String key, boolean value) {
        }

        // TODO: Add Enum support

        // TODO: Add List<?>/Set support

        // TODO: Add (ARGB) color picker support
    }

    public static class OptionBuilder {

        public OptionBuilder() {
        }

        public void comment(String comment) {
        }

        public void addCategory(String name) {
        }

        // TODO: Add actual stuff for the addOptions

        public void attachImage(Path path) {
        }
    }
}
