package net.justmili.mcl.v2.lib.config.codecs

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.*
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.options.ConfigOption

object ConfigCodec {

    /**
     * @param asSynced decodes values as the server's synced values instead of the option's own
     */
    @JvmStatic
    @JvmOverloads
    fun of(root: CategoryItem, asSynced: Boolean = false): Codec<CategoryItem> {
        val encoder = object : Encoder<CategoryItem> {
            override fun <T> encode(input: CategoryItem, ops: DynamicOps<T>, prefix: T): DataResult<T> {
                return encodeCat(input, ops, prefix)
            }
        }

        val decoder = object : Decoder<CategoryItem> {
            override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<CategoryItem, T>> {
                return ops.getMap(input).map { map ->
                    decodeCat(root, ops, map, asSynced)
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

    private fun <T> decodeCat(category: CategoryItem, ops: DynamicOps<T>, map: MapLike<T>, asSynced: Boolean) {
        for (item in category.children) {
            if (item is ConfigOption<*>) {
                val value = map.get(item.key)
                val loaded = value != null && (if (asSynced) item.loadSynced(ops, value) else item.load(ops, value))
                if (value != null && !loaded) MConfigLogger.invalidValue(item)
                if (asSynced && !loaded) item.clearSynced()
            } else if (item is CategoryItem) {
                val value = map.get(item.name)
                val childMap = if (value != null) ops.getMap(value).result().orElse(null) else null
                if (childMap != null) decodeCat(item, ops, childMap, asSynced)
                else if (asSynced) item.getChildren().forEach { option -> option.clearSynced() }
            }
        }
    }
}