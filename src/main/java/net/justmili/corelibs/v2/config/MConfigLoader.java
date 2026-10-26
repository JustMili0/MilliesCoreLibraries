package net.justmili.corelibs.v2.config;

import net.fabricmc.loader.api.FabricLoader;
import net.justmili.corelibs.v2.config.writers.FormatWriter;
import net.justmili.corelibs.v2.config.writers.json.Json5Writer;
import net.justmili.corelibs.v2.config.writers.json.JsonWriter;
import net.justmili.corelibs.v2.config.writers.properties.PropsWriter;
import net.justmili.corelibs.v2.config.flags.ConfigTypes;
import net.justmili.corelibs.v2.config.flags.FileTypes;
import net.justmili.corelibs.v2.config.writers.text.TextWriter;

import java.nio.file.Path;

public class MConfigLoader {
    private final String modId;
    private final String fileName;
    private final String suffix;


    public MConfigLoader(String modId, String customFileName, ConfigTypes configType, FileTypes fileType, boolean createSubDirectory) {
        var configDir = getConfigDir();
        this.modId = modId;
        this.fileName = customFileName == null? modId : customFileName;
        this.suffix = configType.suffix();
    }

    private FormatWriter getWriter(FileTypes fileType) {
        return switch (fileType) {
            case TEXT -> new TextWriter();
            case JSON -> new JsonWriter();
            case JSON5 -> new Json5Writer();
            case PROPERTIES -> new PropsWriter();
            case TOML -> throw new UnsupportedOperationException("TOML support is not yet fully implemented.");
        };
    }

    private String getExtension(FileTypes fileType) {
        return fileType.extension();
    }

    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
