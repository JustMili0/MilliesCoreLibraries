package net.justmili.corelibs.v2.config.writers.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import net.justmili.corelibs.CoreLibs;
import net.justmili.corelibs.v2.config.ConfigCodec;
import net.justmili.corelibs.v2.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.items.CommentItem;
import net.justmili.corelibs.v2.config.items.ConfigItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SharedJsonIO {
    public static JsonObject encode(CategoryItem root) {
        var encoded = ConfigCodec.of(root).encodeStart(JsonOps.INSTANCE, root)
            .resultOrPartial(e -> CoreLibs.LOGGER.error("Failed to encode config: {}", e));

        return encoded.isPresent() && encoded.get() instanceof JsonObject json? json : new JsonObject();
    }

    public static boolean load(Path path, CategoryItem root) {
        try (var reader = Files.newBufferedReader(path)) {
            return ConfigCodec.of(root).parse(JsonOps.INSTANCE, JsonParser.parseReader(reader))
                .resultOrPartial(e -> CoreLibs.LOGGER.error("Failed to load config: {}", e))
                .isPresent();
        } catch (IOException | JsonParseException e) {
            CoreLibs.LOGGER.error("Failed to load config: {}", e.getMessage());
            return false;
        }
    }

    public static String jsonValue(JsonObject json, String key) {
        return json.has(key)? json.get(key).toString() : "null";
    }

    public static JsonObject child(JsonObject json, String name) {
        return json.get(name) instanceof JsonObject child? child : new JsonObject();
    }

    public static String quote(String text) {
        return new JsonPrimitive(text).toString();
    }

    public static int lastRealIndex(List<ConfigItem> items, boolean hasComments) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (hasComments || !(items.get(i) instanceof CommentItem)) return i;
        }
        return -1;
    }
}