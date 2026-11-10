package net.justmili.mcl.v2.lib.config.codecs

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.*
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.options.ConfigOption

object ConfigCodec {

    @JvmStatic
    fun of(root: CategoryItem): Codec<CategoryItem> {
        val encoder = object : Encoder<CategoryItem> {
            override fun <T> encode(input: CategoryItem, ops: DynamicOps<T>, prefix: T): DataResult<T> {
                return encodeCat(input, ops, prefix)
            }
        }

        val decoder = object : Decoder<CategoryItem> {
            override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<CategoryItem, T>> {
                return ops.getMap(input).map { map ->
                    decodeCat(root, ops, map)
                    Pair.of(root, input)
                }
            }
        }

        return Codec.of(encoder, decoder)
    }

    private fun <T> encodeCat(category: CategoryItem, ops: DynamicOps<T>, prefix: T): DataResult<T> {
        val builder = ops.mapBuilder()
        for (item in category.children) {
            if (item is ConfigOption<*>) {
                item.encode(ops).ifPresent { value -> builder.add(item.key, value) }
            } else if (item is CategoryItem) {
                encodeCat(item, ops, ops.empty()).result().ifPresent { value -> builder.add(item.name, value) }
            }
        }
        return builder.build(prefix)
    }

    private fun <T> decodeCat(category: CategoryItem, ops: DynamicOps<T>, map: MapLike<T>) {
        for (item in category.children) {
            if (item is ConfigOption<*>) {
                val value = map.get(item.key)
                if (value != null && !item.load(ops, value)) MConfigLogger.invalidValue(item)
            } else if (item is CategoryItem) {
                val value = map.get(item.name)
                if (value != null) ops.getMap(value).result().ifPresent { childMap -> decodeCat(item, ops, childMap) }
            }
        }
    }
}