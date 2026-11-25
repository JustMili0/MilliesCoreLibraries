package net.justmili.mcl.v2.lib.codec

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import java.util.List.copyOf

object ListCodec {

    @JvmStatic
    fun <E : Any> of(entry: Codec<E>, collapse: Boolean): Codec<List<E>> {
        return Codec.either(LenientListCodec(entry), entry).xmap<List<E>>(
            { either -> normalize(either.map<List<E>>({ list -> list }, { single -> listOf(single) }), collapse) },
            { list -> Either.left(list) }
        )
    }

    @JvmStatic
    fun <E : Any> normalize(list: List<E>, collapse: Boolean): List<E> {
        if (!collapse) return copyOf(list)
        val unique = LinkedHashSet(list)
        return copyOf(if (unique.size == list.size) list else unique)
    }
}