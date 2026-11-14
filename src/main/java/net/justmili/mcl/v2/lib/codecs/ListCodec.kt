package net.justmili.mcl.v2.lib.codecs

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.*
import java.util.List.copyOf

object ListCodec {

    @JvmStatic
    fun <E : Any> of(entry: Codec<E>, collapse: Boolean): Codec<List<E>> {
        val decoder = object : Decoder<List<E>> {
            override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<List<E>, T>> {
                val stream = ops.getList(input)
                if (stream.result().isEmpty) {
                    val single = entry.parse(ops, input).result().orElse(null)
                    if (single != null) return DataResult.success(Pair.of(normalize(listOf(single), collapse), input))
                }

                return stream.map { entries ->
                    val valid = ArrayList<E>()
                    entries.accept { input ->
                        val value = entry.parse(ops, input).result().orElse(null)
                        if (value != null) valid.add(value)
                    }
                    Pair.of(normalize(valid, collapse), input)
                }
            }
        }
        return Codec.of(entry.listOf(), decoder)
    }

    @JvmStatic
    fun <E : Any> normalize(list: List<E>, collapse: Boolean): List<E> {
        if (!collapse) return copyOf(list)
        val unique = LinkedHashSet(list)
        return copyOf(if (unique.size == list.size) list else unique)
    }
}