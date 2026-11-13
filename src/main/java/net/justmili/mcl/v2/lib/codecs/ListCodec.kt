package net.justmili.mcl.v2.lib.codecs

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.*
import net.justmili.mcl.v2.lib.config.MConfigLogger
import java.util.List.copyOf

object ListCodec {

    @JvmStatic
    fun <E : Any> of(element: Codec<E>, collapse: Boolean): Codec<List<E>> {
        val decoder = object : Decoder<List<E>> {
            override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<List<E>, T>> {
                return ops.getList(input).map { stream ->
                    val valid = ArrayList<E>()
                    stream.accept { entry ->
                        val value = element.parse(ops, entry).result().orElse(null)
                        if (value != null) valid.add(value)
                    }
                    Pair.of(normalize(valid, collapse), input)
                }
            }
        }
        return Codec.of(element.listOf(), decoder)
    }

    @JvmStatic
    fun <E : Any> normalize(list: List<E>, collapse: Boolean): List<E> {
        if (!collapse) return copyOf(list)
        val unique = LinkedHashSet(list)
        return copyOf(if (unique.size == list.size) list else unique)
    }
}