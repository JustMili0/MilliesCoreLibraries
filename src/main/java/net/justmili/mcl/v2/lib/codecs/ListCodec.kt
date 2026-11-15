package net.justmili.mcl.v2.lib.codecs

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.*
import java.util.List.copyOf

object ListCodec {

    @JvmStatic
    fun <E : Any> of(entry: Codec<E>, collapse: Boolean): Codec<List<E>> {
        val lenientList = Codec.of(entry.listOf(), object : Decoder<List<E>> {
            override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<List<E>, T>> {
                return ops.getList(input).map { entries ->
                    val valid = ArrayList<E>()
                    entries.accept { input ->
                        val value = entry.parse(ops, input).result().orElse(null)
                        if (value != null) valid.add(value)
                    }
                    Pair.of(valid as List<E>, input)
                }
            }
        })

        return Codec.either(lenientList, entry).xmap<List<E>>(
            { either -> normalize(either.map<List<E>>({ list -> list }, { single -> listOf(single) }), collapse) },
            { list -> if (list.size == 1) Either.right(list[0]) else Either.left(list) }
        )
    }

    @JvmStatic
    fun <E : Any> normalize(list: List<E>, collapse: Boolean): List<E> {
        if (!collapse) return copyOf(list)
        val unique = LinkedHashSet(list)
        return copyOf(if (unique.size == list.size) list else unique)
    }
}