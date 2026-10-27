package net.justmili.corelibs.v2.config.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.justmili.corelibs.CoreLibs;
import net.justmili.corelibs.v2.config.flags.OptionFlags;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConfigOption<T> implements ConfigItem {
    private final String key;
    private final T defaultValue;
    private final Codec<T> codec;
    private final Predicate<T> validator;
    private final String restriction;
    private final Set<OptionFlags> flags = EnumSet.noneOf(OptionFlags.class);
    private String comment;
    private final Consumer<T> setter;
    private T value;

    public ConfigOption(String key, T defaultValue, Codec<T> codec, Predicate<T> validator, String restriction, Consumer<T> setter, OptionFlags... flags) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.codec = codec;
        this.validator = validator;
        this.restriction = restriction;
        this.setter = setter;
        this.value = defaultValue;
        Collections.addAll(this.flags, flags);
        setter.accept(value);
    }

    public String key() {
        return key;
    }

    public T defaultValue() {
        return defaultValue;
    }

    public T get() {
        return value;
    }

    public void comment(String comment) {
        this.comment = comment;
    }

    public String comment() {
        return comment;
    }

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
        setter.accept(value);
        return true;
    }
}