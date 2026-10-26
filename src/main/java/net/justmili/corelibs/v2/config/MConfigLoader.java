package net.justmili.corelibs.v2.config;

import net.fabricmc.loader.api.FabricLoader;
import net.justmili.corelibs.v2.config.writers.FormatWriter;
import net.justmili.corelibs.v2.config.writers.json.Json5Writer;
import net.justmili.corelibs.v2.config.writers.json.JsonWriter;
import net.justmili.corelibs.v2.config.writers.properties.PropsWriter;
import net.justmili.corelibs.v2.config.flags.ConfigTypes;
import net.justmili.corelibs.v2.config.flags.FileTypes;

import java.nio.file.Path;

public class MConfigLoader {
    private final String modId;
    private final String fileName;
    private final String suffix;
    private final FormatWriter writer;
    private final Path path;


    public MConfigLoader(String modId, String customFileName, ConfigTypes configType, FileTypes fileType, boolean createSubDirectory) {
        var configDir = getConfigDir();

        this.modId = modId;
        this.fileName = customFileName == null? modId : customFileName;
        this.suffix = configType.suffix();
        this.writer = getWriter(fileType);

        if (configType == ConfigTypes.COMMON_CLIENT_PRIORITY) {
            // Client -> Server
        } else if (configType == ConfigTypes.COMMON_SERVER_PRIORITY) {
            // Server -> Client
        }

        var extension = fileType.extension();
        var file = createSubDirectory? suffix : modId + "-" + suffix; // It's gotten confusing with the custom file name, idk man

        // TODO: "File name matches COMMON unless a COMMON config already exists." -Priority Config Types
        //configType.altSuffix();

        path = createSubDirectory
            ? configDir.resolve(modId).resolve(file + extension)
            : configDir.resolve(file + extension);
    }

    private FormatWriter getWriter(FileTypes fileType) {
        return switch (fileType) {
            case JSON -> new JsonWriter();
            case JSON5 -> new Json5Writer();
            case PROPERTIES -> new PropsWriter();
            case TOML -> throw new UnsupportedOperationException("TOML support is not yet fully implemented.");
        };
    }

    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
