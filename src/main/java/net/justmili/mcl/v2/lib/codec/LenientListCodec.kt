package net.justmili.mcl.v2.lib.codec

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.*

class LenientListCodec<E : Any>(private val entry: Codec<E>) : Codec<List<E>> {
    private val list = entry.listOf()

    override fun <T> encode(input: List<E>, ops: DynamicOps<T>, prefix: T): DataResult<T> = list.encode(input, ops, prefix)

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

    override fun toString() = "Codec[$list]"
}