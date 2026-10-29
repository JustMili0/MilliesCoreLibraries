package net.justmili.corelibs.v2.config;

import net.justmili.corelibs.CoreLibs;
import net.justmili.corelibs.v1.util.Platform;
import net.justmili.corelibs.v2.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.writers.FileWriter;
import net.justmili.corelibs.v2.config.writers.json.Json5IO;
import net.justmili.corelibs.v2.config.writers.json.JsonIO;
import net.justmili.corelibs.v2.config.writers.properties.PropertiesIO;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

public class MConfigLoader {
    private static final Set<Path> CLAIMED_PATHS = new HashSet<>();
    private final CategoryItem root = new CategoryItem("root", null);
    private final String modId;
    private final String fileName;
    private final String suffix;
    private final FileWriter writer;
    private final Path path;

    public MConfigLoader(String modId, @Nullable String customFileName, ConfigTypes configType, FileTypes fileType, boolean createSubDirectory) {
        var configDir = Platform.getConfigDir();

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

        if (!CLAIMED_PATHS.add(path)) CoreLibs.LOGGER.warn("Config file '{}' of '{}' is already used by another config, they will overwrite each other.", path, modId);
    }

    public CategoryItem root() {
        return root;
    }

    public void save() {
        writer.write(path, root);
    }

    public void buildOrLoad() {
        if (!path.toFile().exists()) {
            save();
            return;
        }

        if (!writer.load(path, root)) backupIfBroken();
        save();
    }

    private void backupIfBroken() {
        var backupPath = path.resolveSibling(path.getFileName() + ".mclb"); // MCLB - MConfigLoader Backup
        try {
            Files.move(path, backupPath, StandardCopyOption.REPLACE_EXISTING);
            CoreLibs.LOGGER.warn("Could not read config of '{}' ({}, {}). File has been backed up to {}, but default values will be used.", modId, path, suffix, backupPath);
        } catch (IOException e) {
            CoreLibs.LOGGER.error("Failed to back up unreadable config of '{}' ({}): {}", modId, suffix, e.getMessage());
        }
    }

    private FileWriter getWriter(FileTypes fileType) {
        return switch (fileType) {
            case JSON -> new JsonIO();
            case JSON5 -> new Json5IO();
            case PROPERTIES -> new PropertiesIO();
            case TOML -> throw new UnsupportedOperationException("TOML support is not yet fully implemented.");
        };
    }

    private Path getPath(Path directory, String suffix, String extension, boolean prefixWithFileName) {
        var file = prefixWithFileName? this.fileName + "-" + suffix : suffix;
        return directory.resolve(file + extension);
    }

    public static void loadError(String e) {
        CoreLibs.LOGGER.error("Failed to load config: {}", e);
    }
}