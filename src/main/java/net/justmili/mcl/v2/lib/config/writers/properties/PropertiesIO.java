package net.justmili.mcl.v2.lib.config.writers.properties;

import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v1.lib.util.Platform;
import net.justmili.mcl.v1.lib.util.common.FileIO;
import net.justmili.mcl.v2.lib.config.MConfigLoader;
import net.justmili.mcl.v2.lib.config.items.*;
import net.justmili.mcl.v2.lib.config.writers.FileWriter;

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
                    CoreLibs.LOGGER.warn("Categories are not supported in properties config format - they will not exist in the file.");
                    warnedAboutCategories = true;
                }
                writeDescComment(writer, cat.comment(), TOKEN_POUND, NO_INDENT);
                writeItems(writer, cat.children(), warnedAboutCategories);

            } else if (item instanceof CommentItem comment) {
                writeDescComment(writer, comment.text, TOKEN_POUND, NO_INDENT);

            } else if (item instanceof ConfigOption<?> option) {
                writeOptionComment(writer, option, TOKEN_POUND, NO_INDENT);
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