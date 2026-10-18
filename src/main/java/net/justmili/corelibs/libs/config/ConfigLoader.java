package net.justmili.corelibs.libs.config;

import net.justmili.corelibs.CoreLibs;
import net.justmili.corelibs.libs.config.entry.ConfigEntry;
import net.justmili.corelibs.libs.config.entry.ListConfigEntry;
import net.justmili.corelibs.libs.config.items.CategoryItem;
import net.justmili.corelibs.libs.config.sync.SyncConfigCSP;
import net.justmili.corelibs.libs.config.writers.FormatWriter;
import net.justmili.corelibs.libs.config.writers.json.Json5Writer;
import net.justmili.corelibs.libs.config.writers.json.JsonWriter;
import net.justmili.corelibs.libs.config.writers.properties.PropsWriter;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {
    public final String modId;
    public final String suffix;
    private final Path path;
    private final FormatWriter writer;
    public final Map<String, ConfigEntry<?>> entries = new HashMap<>();
    public final Map<String, ListConfigEntry> listEntries = new HashMap<>();
    public CategoryItem root;

    public ConfigLoader(String modId, ConfigType configType, FileType fileType, boolean createSubDirectory) {
        Path configDirectory = Path.of("config");
        this.modId = modId;
        this.suffix = switch (configType) {
            case CLIENT -> "client";
            case SERVER -> "server";
            case COMMON -> "common";
            case COMMON_SERVER_PRIORITY -> "common-sp";
        };
        if (configType == ConfigType.COMMON_SERVER_PRIORITY) {
            SyncConfigCSP.register(this);
        }
        this.writer = resolveWriter(fileType);

        String extension = extension(fileType);
        String fileName = createSubDirectory ? suffix : modId+"-"+ suffix;

        path = createSubDirectory
            ? configDirectory.resolve(modId).resolve(fileName+extension)
            : configDirectory.resolve(fileName+extension);
    }

    public void register(ConfigEntry<?> entry) {
        entries.put(entry.key(), entry);
    }

    public void registerList(ListConfigEntry entry) {
        listEntries.put(entry.key(), entry);
    }

    public void loadOrCreate(CategoryItem root) {
        this.root = root;
        File file = path.toFile();
        if (!file.exists()) {
            CoreLibs.LOGGER.info("No config found, creating defaults.");
            writer.write(path, root);
            return;
        }
        writer.load(path, entries, listEntries);
        writer.write(path, root);
    }

    public void save() {
        writer.write(path, root);
    }

    private static FormatWriter resolveWriter(FileType fileType) {
        return switch (fileType) {
            case JSON -> new JsonWriter();
            case JSON5 -> new Json5Writer();
            case TOML -> throw new UnsupportedOperationException("TOML support is not yet implemented.");
            default -> new PropsWriter();
        };
    }

    private static String extension(FileType fileType) {
        return switch (fileType) {
            case JSON -> ".json";
            case JSON5 -> ".json5";
            case TOML -> ".toml";
            default -> ".properties";
        };
    }
}