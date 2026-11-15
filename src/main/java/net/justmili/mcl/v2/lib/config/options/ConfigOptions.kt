package net.justmili.mcl.v2.lib.config.options

import com.google.gson.*
import com.mojang.serialization.*
import net.justmili.mcl.v2.lib.codecs.ListCodec
import net.justmili.mcl.v2.lib.config.MConfigLoader
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.items.ConfigItem
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs.TagOrElementLocation
import java.nio.file.Path
import java.util.*

open class ConfigOption<O : Any>(val key: String, val default: O, val codec: Codec<O>, val limit: OptionBounds<O>?, val flagId: Int, private val isText: Boolean, private val loader: MConfigLoader) : ConfigItem {
    @Volatile protected var value: O = default
    @Volatile private var synced: O? = null
    var attachedImage: ResourceLocation? = null
    var comment: String? = null
    var oldKey: String? = null
    val path: Path get() = loader.path
    val bounds: String? get() = limit?.describe()

    fun get() = synced ?: value

    open fun set(value: O): Boolean {
        val clamped = clamp(value)
        if (this.value == clamped) return true
        this.value = clamped
        loader.tryWrite()
        return true
    }

    open fun setSynced(value: O): Boolean {
        this.synced = clamp(value)
        return true
    }

    fun clearSynced() {
        this.synced = null
    }

    fun isSynced() = synced != null

    fun hasFlag(flag: OptionFlags) = (flagId and flag.flagId) != 0

    fun <R> encode(ops: DynamicOps<R>): Optional<R> = codec.encodeStart(ops, value).resultOrPartial { error -> MConfigLogger.encodeFail(this, error) }

    fun <R> load(ops: DynamicOps<R>, input: R): Boolean {
        val decoded = codec.parse(ops, input).result().orElse(null) ?: return false
        val clamped = clamp(decoded)
        this.value = clamped
        return true
    }

    fun toText() = textOf(value)

    private fun textOf(value: O): String {
        val element = codec.encodeStart(JsonOps.INSTANCE, value).resultOrPartial { error -> MConfigLogger.encodeFail(this, error) }.orElse(null) ?: return ""
        return if (element is JsonPrimitive && value !is Collection<*>) element.asString else element.toString()
    }

    internal fun getDefaultsComment(): String {
        return when (default) {
            is String, is Number, is Boolean -> default.toString()
            is Collection<*> -> if (default.isEmpty()) "" else textOf(default)
            else -> textOf(default)
        }
    }

    internal fun loadText(raw: String): Boolean {
        return load(JsonOps.INSTANCE, when {
            default is String -> JsonPrimitive(raw)
            isText -> JsonPrimitive(raw.trim())
            else -> parseText(raw)
        })
    }

    private fun clamp(value: O) = limit?.clamp(value) ?: value

    private fun parseText(raw: String): JsonElement {
        return try {
            JsonParser.parseString(raw.trim())
        } catch (e: JsonParseException) {
            JsonPrimitive(raw)
        }
    }

    override fun toString() = "ConfigOption<$codec>($key)[$value]"
}

open class ListConfigOption<E : Any>(key: String, default: List<E>, codec: Codec<List<E>>, limit: OptionBounds<List<E>>?, flagId: Int, loader: MConfigLoader, private val collapse: Boolean)
    : ConfigOption<@JvmSuppressWildcards List<E>>(key, default, codec, limit, flagId, false, loader) {
    override fun toString() = "ListConfigOption<$codec>($key)[$value]"
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

class HolderSetConfigOption<E : Any>(key: String, default: List<TagOrElementLocation>, codec: Codec<List<TagOrElementLocation>>, limit: OptionBounds<List<TagOrElementLocation>>?, flagId: Int, loader: MConfigLoader, private val registry: Registry<E>)
    : ListConfigOption<TagOrElementLocation>(key, default, codec, limit, flagId, loader, true) {
    override fun toString() = "HolderSetConfigOption<$codec>($key)[$value]"
    fun contains(value: E) = contains(registry.wrapAsHolder(value))
    fun contains(holder: Holder<E>): Boolean {
        return get().any { entry -> if (entry.tag()) holder.`is`(TagKey.create(registry.key(), entry.id())) else holder.`is`(entry.id()) }
    }
}