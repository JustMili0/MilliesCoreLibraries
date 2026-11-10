package net.justmili.mcl.v2.lib.config.options

import com.mojang.serialization.Codec
import net.justmili.mcl.v2.lib.codecs.ListCodec
import net.justmili.mcl.v2.lib.config.MConfigLoader

class ListConfigOption<E : Any>(key: String, default: List<E>, codec: Codec<List<E>>, limit: OptionBounds<List<E>>?, loader: MConfigLoader, flagMask: Int, private val collapse: Boolean) : ConfigOption<@JvmSuppressWildcards List<E>>(key, default, codec, limit, loader, flagMask, false) {

    override fun set(value: List<E>) = super.set(ListCodec.normalize(value, collapse))

    override fun setSynced(value: List<E>) = super.setSynced(ListCodec.normalize(value, collapse))

    fun addToList(vararg elements: E) {
        synchronized(this) {
            set(value + elements)
        }
    }

    fun removeFromList(vararg elements: E) {
        synchronized(this) {
            set(value - elements.toSet())
        }
    }
}