package net.justmili.mcl.v2.lib.config.writers.json;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v2.lib.config.MConfigLoader;
import net.justmili.corelibs.v2.config.items.*;
import net.justmili.mcl.v2.config.items.*;
import net.justmili.mcl.v2.lib.config.items.*;
import net.justmili.mcl.v2.lib.config.writers.ConfigCodec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SharedJsonIO {

    public static JsonObject encode(CategoryItem root) {
        var encoded = ConfigCodec.of(root).encodeStart(JsonOps.INSTANCE, root).resultOrPartial(e -> CoreLibs.LOGGER.error("Failed to encode config: {}", e));
        return encoded.isPresent() && encoded.get() instanceof JsonObject json? json : new JsonObject();
    }

    public static boolean load(Path path, CategoryItem root) {
        try (var reader = Files.newBufferedReader(path)) {
            return ConfigCodec.of(root).parse(JsonOps.INSTANCE, JsonParser.parseReader(reader)).resultOrPartial(MConfigLoader::loadError).isPresent();
        } catch (IOException | JsonParseException e) {
            MConfigLoader.loadError(e.getMessage());
            return false;
        }
    }

    public static String getValue(JsonObject obj, String key) {
        return obj.has(key)? obj.get(key).toString() : "null";
    }

    public static JsonObject getChild(JsonObject obj, String name) {
        return obj.get(name) instanceof JsonObject child? child : new JsonObject();
    }

    public static String quote(String text) {
        return new JsonPrimitive(text).toString();
    }

    public static int lastRealIndex(List<ConfigItem> items) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (!(items.get(i) instanceof CommentItem)) return i;
        }
        return -1;
    }
}