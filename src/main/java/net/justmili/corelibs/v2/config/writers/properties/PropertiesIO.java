package net.justmili.corelibs.v2.config.writers.properties;

import net.justmili.corelibs.CoreLibs;
import net.justmili.corelibs.v1.util.Platform;
import net.justmili.corelibs.v1.util.common.FileIO;
import net.justmili.corelibs.v2.config.MConfigLoader;
import net.justmili.corelibs.v2.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.items.CommentItem;
import net.justmili.corelibs.v2.config.items.ConfigItem;
import net.justmili.corelibs.v2.config.items.ConfigOption;
import net.justmili.corelibs.v2.config.writers.FileWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class PropertiesIO implements FileWriter {

    @Override
    public void write(Path path, CategoryItem root) {
        FileIO.write(path, writer -> {
            writer.write(TOKEN_POUND + path.getFileName() + "\n\n");
            writeItems(writer, root.children(), false);
        });
    }

    private void writeItems(BufferedWriter writer, List<ConfigItem> items, boolean warnedAboutCategories) throws IOException {
        for (var item : items) {
            if (item instanceof CategoryItem cat) {
                if (!warnedAboutCategories && Platform.IS_DEV_ENV) {
                    CoreLibs.LOGGER.warn("Categories not supported in .properties format - flattening.");
                    warnedAboutCategories = true;
                }
                writeComment(writer, cat.comment(), TOKEN_POUND, "");
                writeItems(writer, cat.children(), warnedAboutCategories);

            } else if (item instanceof CommentItem comment) {
                writeComment(writer, comment.comment, TOKEN_POUND, "");

            } else if (item instanceof ConfigOption<?> option) {
                writeComment(writer, option.comment(), TOKEN_POUND, "");
                writer.write(hint(option, TOKEN_POUND) + "\n");
                writer.write(option.key() + "=" + escape(option.toText()) + "\n\n");
            }
        }
    }

    @Override
    public boolean load(Path path, CategoryItem root) {
        var properties = new Properties();
        try (var reader = Files.newBufferedReader(path)) {
            properties.load(reader);
        } catch (IOException e) {
            MConfigLoader.loadError(e.getMessage());
            return false;
        }

        loadItems(properties, root.children());
        return true;
    }

    private void loadItems(Properties properties, List<ConfigItem> items) {
        for (var item : items) {
            if (item instanceof CategoryItem cat) {
                loadItems(properties, cat.children());
            } else if (item instanceof ConfigOption<?> option) {
                var raw = properties.getProperty(option.key());
                if (raw != null && !option.loadText(raw)) CoreLibs.LOGGER.warn("Invalid value '{}' for config option '{}', using default.", raw, option.key());
            }
        }
    }

    private String escape(String text) {
        return text.replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
    }
}