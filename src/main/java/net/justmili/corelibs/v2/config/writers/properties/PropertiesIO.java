package net.justmili.corelibs.v2.config.writers.properties;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.justmili.corelibs.CoreLibs;
import net.justmili.corelibs.v1.util.Platform;
import net.justmili.corelibs.v1.util.common.FileIO;
import net.justmili.corelibs.v2.config.MConfigLoader;
import net.justmili.corelibs.v2.config.writers.ConfigCodec;
import net.justmili.corelibs.v2.config.flags.CommentType;
import net.justmili.corelibs.v2.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.items.CommentItem;
import net.justmili.corelibs.v2.config.items.ConfigItem;
import net.justmili.corelibs.v2.config.items.ConfigOption;
import net.justmili.corelibs.v2.config.writers.FileWriter;
import net.justmili.corelibs.v2.config.writers.json.SharedJsonIO;

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
            writer.write(CommentType.POUND.token() + path.getFileName() + "\n\n");
            writeItems(writer, root.children(), SharedJsonIO.encode(root), false);
        });
    }

    private void writeItems(BufferedWriter writer, List<ConfigItem> items, JsonObject obj, boolean warnedAboutCategories) throws IOException {
        for (var item : items) {
            if (item instanceof CategoryItem cat) {
                if (!warnedAboutCategories && Platform.IS_DEV_ENV) {
                    CoreLibs.LOGGER.warn("Categories not supported in .properties format - flattening.");
                    warnedAboutCategories = true;
                }
                writeComment(writer, cat.comment(), CommentType.POUND, "");
                writeItems(writer, cat.children(), SharedJsonIO.getChild(obj, cat.name()), warnedAboutCategories);

            } else if (item instanceof CommentItem commentItem) {
                writeComment(writer, commentItem.comment(), CommentType.POUND, "");

            } else if (item instanceof ConfigOption<?> option) {
                writeComment(writer, option.comment(), CommentType.POUND, "");
                writer.write(hint(option, CommentType.POUND) + "\n");
                writer.write(option.key() + "=" + toText(obj.get(option.key())) + "\n\n");
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

        return ConfigCodec.of(root).parse(JsonOps.INSTANCE, toJson(properties, root.children())).resultOrPartial(MConfigLoader::loadError).isPresent();
    }

    private JsonObject toJson(Properties properties, List<ConfigItem> items) {
        var obj = new JsonObject();
        for (var item : items) {
            if (item instanceof CategoryItem cat) {
                obj.add(cat.name(), toJson(properties, cat.children()));
            } else if (item instanceof ConfigOption<?> option) {
                var raw = properties.getProperty(option.key());
                if (raw != null) obj.add(option.key(), toElement(option, raw));
            }
        }
        return obj;
    }

    private String toText(JsonElement element) {
        if (element == null) return "";
        return element instanceof JsonPrimitive primitive? escape(primitive.getAsString()) : element.toString();
    }

    private JsonElement toElement(ConfigOption<?> option, String raw) {
        if (option.defaultValue() instanceof String) return new JsonPrimitive(raw);

        try {
            return JsonParser.parseString(raw.trim());
        } catch (JsonParseException e) {
            return new JsonPrimitive(raw);
        }
    }

    private String escape(String text) {
        return text.replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
    }
}