package net.justmili.mcl.v2.lib.codecs

import com.mojang.serialization.Codec
import java.util.List.copyOf

object ListCodec {

    @JvmStatic
    fun <E : Any> of(element: Codec<E>, collapse: Boolean): Codec<List<E>> {
        return element.listOf().xmap<List<E>>({ list -> normalize(list, collapse) }, { list -> list })
    }

    @JvmStatic
    fun <E : Any> normalize(list: List<E>, collapse: Boolean): List<E> {
        if (!collapse) return copyOf(list)
        val unique = LinkedHashSet(list)
        return copyOf(if (unique.size == list.size) list else unique)
    }
}