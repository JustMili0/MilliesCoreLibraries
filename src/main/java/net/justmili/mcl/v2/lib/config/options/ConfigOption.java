package net.justmili.mcl.v2.lib.config.options;

import com.google.gson.*;
import com.mojang.serialization.*;
import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v2.lib.config.items.ConfigItem;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class ConfigOption<T> implements ConfigItem {
    private final String key;
    private final T defaultValue;
    private final Codec<T> codec;
    private final Predicate<T> validator;
    private final String restriction;
    private final Runnable saver;
    private final Set<OptionFlags> flags = EnumSet.noneOf(OptionFlags.class);
    private String comment;
    private volatile T value;
    private volatile T synced;

    public ConfigOption(String key, T defaultValue, Codec<T> codec, Predicate<T> validator, @Nullable String restriction, Runnable saver, OptionFlags... flags) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.codec = codec;
        this.validator = validator;
        this.restriction = restriction;
        this.saver = saver;
        this.value = defaultValue;
        Collections.addAll(this.flags, flags);
    }

    public String key() {
        return key;
    }

    public T getDefault() {
        return defaultValue;
    }

    public T get() {
        var current = synced;
        return current != null? current : value;
    }

    public boolean set(T value) {
        if (!validate(value)) return false;
        if (value.equals(this.value)) return true;

        this.value = value;
        saver.run();
        return true;
    }

    public boolean setSynced(T value) {
        if (!validate(value)) return false;

        this.synced = value;
        return true;
    }

    public void clearSynced() {
        this.synced = null;
    }

    public boolean isSynced() {
        return synced != null;
    }

    public void comment(String comment) {
        this.comment = comment;
    }

    @Nullable
    public String comment() {
        return comment;
    }

    @Nullable
    public String restriction() {
        return restriction;
    }

    public Set<OptionFlags> flags() {
        return Collections.unmodifiableSet(flags);
    }

    public <R> Optional<R> encode(DynamicOps<R> ops) {
        return codec.encodeStart(ops, value).resultOrPartial(error -> CoreLibs.LOGGER.warn("Failed to encode config option '{}': {}", key, error));
    }

    public <R> boolean load(DynamicOps<R> ops, R input) {
        var decoded = codec.parse(ops, input).result().filter(validator);
        if (decoded.isEmpty()) return false;

        this.value = decoded.get();
        return true;
    }

    public String toText() {
        return encode(JsonOps.INSTANCE).map(element -> element instanceof JsonPrimitive primitive? primitive.getAsString() : element.toString()).orElse("");
    }

    public boolean loadText(String raw) {
        return load(JsonOps.INSTANCE, defaultValue instanceof String? new JsonPrimitive(raw) : parseText(raw));
    }

    private boolean validate(@Nullable T value) {
        return defaultValue.getClass().isInstance(value) && validator.test(value);
    }

    private JsonElement parseText(String raw) {
        try {
            return JsonParser.parseString(raw.trim());
        } catch (JsonParseException e) {
            return new JsonPrimitive(raw);
        }
    }
}