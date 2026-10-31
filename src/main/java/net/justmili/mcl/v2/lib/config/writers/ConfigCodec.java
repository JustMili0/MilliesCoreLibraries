package net.justmili.mcl.v2.lib.config.writers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.justmili.mcl.CoreLibs;
import net.justmili.mcl.v2.lib.config.items.CategoryItem;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;

public class ConfigCodec {

    public static Codec<CategoryItem> of(CategoryItem root) {
        return Codec.of(ConfigCodec::encodeCat, new Decoder<>() {

            @Override
            public <T> DataResult<Pair<CategoryItem, T>> decode(DynamicOps<T> ops, T input) {
                return ops.getMap(input).map(map -> {
                    decodeCat(root, ops, map);
                    return Pair.of(root, input);
                });
            }
        });
    }

    private static <T> DataResult<T> encodeCat(CategoryItem category, DynamicOps<T> ops, T prefix) {
        var builder = ops.mapBuilder();
        for (var item : category.children()) {
            if (item instanceof ConfigOption<?> option) {
                option.encode(ops).ifPresent(value -> builder.add(option.key(), value));
            } else if (item instanceof CategoryItem child) {
                encodeCat(child, ops, ops.empty()).result().ifPresent(value -> builder.add(child.name(), value));
            }
        }
        return builder.build(prefix);
    }

    private static <T> void decodeCat(CategoryItem category, DynamicOps<T> ops, MapLike<T> map) {
        for (var item : category.children()) {
            if (item instanceof ConfigOption<?> option) {
                var value = map.get(option.key());
                if (value != null && !option.load(ops, value)) CoreLibs.LOGGER.warn("Invalid value for config option '{}'.", option.key());
            } else if (item instanceof CategoryItem child) {
                var value = map.get(child.name());
                if (value != null) ops.getMap(value).result().ifPresent(childMap -> decodeCat(child, ops, childMap));
            }
        }
    }
}