package net.justmili.corelibs.v2.config;

import net.fabricmc.loader.api.FabricLoader;
import net.justmili.corelibs.CoreLibs;
import net.justmili.corelibs.v2.config.flags.ConfigTypes;
import net.justmili.corelibs.v2.config.flags.FileTypes;
import net.justmili.corelibs.v2.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.writers.BaseWriter;
import net.justmili.corelibs.v2.config.writers.json.Json5IO;
import net.justmili.corelibs.v2.config.writers.json.JsonIO;
import net.justmili.corelibs.v2.config.writers.properties.PropertiesIO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

public class MConfigLoader {
    private static final Set<Path> CLAIMED_PATHS = new HashSet<>();
    private final CategoryItem root = new CategoryItem("root", null);
    private final String modId;
    private final String fileName;
    private final String suffix;
    private final BaseWriter writer;
    private final Path path;

    public MConfigLoader(String modId, String customFileName, ConfigTypes configType, FileTypes fileType, boolean createSubDirectory) {
        var configDir = getConfigDir();

        this.modId = modId;
        this.fileName = customFileName == null? modId : customFileName;
        this.writer = getWriter(fileType);

        if (configType == ConfigTypes.COMMON_CLIENT_PRIORITY) {
            // Client -> Server
        } else if (configType == ConfigTypes.COMMON_SERVER_PRIORITY) {
            // Server -> Client
        }

        var directory = createSubDirectory? configDir.resolve(modId) : configDir;
        var extension = fileType.extension();
        var prefixWithFileName = customFileName != null || !createSubDirectory;

        var primaryPath = getPath(directory, configType.suffix(), extension, prefixWithFileName);
        var alternativePath = getPath(directory, configType.altSuffix(), extension, prefixWithFileName);

        // Uses alternative suffix for Common Priority config files if a regular common config exists
        var usesAltSuffix = CLAIMED_PATHS.contains(primaryPath) && !primaryPath.equals(alternativePath);
        this.suffix = usesAltSuffix? configType.altSuffix() : configType.suffix();
        this.path = usesAltSuffix? alternativePath : primaryPath;

        if (!CLAIMED_PATHS.add(path)) CoreLibs.LOGGER.warn("Config file '{}' is already used by another config of the same mod, they will overwrite each other.", path);
    }

    public CategoryItem root() {
        return root;
    }

    public void buildOrLoad() {
        if (!path.toFile().exists()) {
            writer.write(path, root);
            return;
        }

        if (!writer.load(path, root)) backupIfBroken();
        writer.write(path, root);
    }

    public void save() {
        writer.write(path, root);
    }

    private void backupIfBroken() {
        var backupPath = path.resolveSibling(path.getFileName() + ".mclb"); // MCLB - MConfigLoader Backup
        try {
            Files.move(path, backupPath, StandardCopyOption.REPLACE_EXISTING);
            CoreLibs.LOGGER.warn("Config '{}' could not be read, it was moved to '{}' and defaults will be used.", path, backupPath);
        } catch (IOException e) {
            CoreLibs.LOGGER.error("Failed to back up unreadable config: {}", e.getMessage());
        }
    }

    private Path getPath(Path directory, String suffix, String extension, boolean prefixWithFileName) {
        var file = prefixWithFileName? this.fileName + "-" + suffix : suffix;
        return directory.resolve(file + extension);
    }

    private BaseWriter getWriter(FileTypes fileType) {
        return switch (fileType) {
            case JSON -> new JsonIO();
            case JSON5 -> new Json5IO();
            case PROPERTIES -> new PropertiesIO();
            case TOML -> throw new UnsupportedOperationException("TOML support is not yet fully implemented.");
        };
    }

    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}