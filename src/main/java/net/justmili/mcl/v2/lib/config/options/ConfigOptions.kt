package net.justmili.mcl.v2.lib.config.options

import com.google.gson.*
import com.mojang.serialization.*
import net.justmili.mcl.v2.lib.codecs.ListCodec
import net.justmili.mcl.v2.lib.config.MConfigLoader
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.items.ConfigItem
import net.minecraft.core.*
import net.minecraft.resources.ResourceKey
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
    val bounds: String? get() = limit?.info()

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

    fun hasFlag(flag: Int) = (flagId and flag) != 0

    fun <R> encode(ops: DynamicOps<R>): Optional<R> = codec.encodeStart(ops, value).resultOrPartial { error -> MConfigLogger.encodeFail(this, error) }

    fun <R> load(ops: DynamicOps<R>, input: R): Boolean {
        val decoded = codec.parse(ops, input).result().orElse(null) ?: return false
        val clamped = clamp(decoded)
        this.value = clamped
        return true
    }

    fun toText() = textOf(value)

    fun textOf(value: O): String {
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

    // Boolean, String with no limit, numbers with no set range and other just print their value, the rest get their limit info from their bounds
    override fun toString() = "ConfigOption<$codec>($key)[value=${toText()}${limit?.info(value)?.let { ", $it" } ?: ""}]"
}

open class ListConfigOption<E : Any>(key: String, default: List<E>, codec: Codec<List<E>>, limit: OptionBounds<List<E>>?, flagId: Int, loader: MConfigLoader, val collapse: Boolean)
    : ConfigOption<@JvmSuppressWildcards List<E>>(key, default, codec, limit, flagId, false, loader) {
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

    override fun toString() = "${javaClass.simpleName}<$codec>($key)[collapse=$collapse, value=$value]"
}

class HolderSetConfigOption<E : Any>(key: String, default: List<TagOrElementLocation>, codec: Codec<List<TagOrElementLocation>>, limit: OptionBounds<List<TagOrElementLocation>>?, flagId: Int, loader: MConfigLoader, private val registry: Registry<E>)
    : ListConfigOption<TagOrElementLocation>(key, default, codec, limit, flagId, loader, true) {
    fun contains(value: E) = contains(registry.wrapAsHolder(value))
    fun contains(holder: Holder<E>): Boolean {
        return get().any { entry -> if (entry.tag()) holder.`is`(TagKey.create(registry.key(), entry.id())) else holder.`is`(entry.id()) }
    }

    /**
     * Gets the values covered by this option as a [HolderSet].
     *
     * All entries are turned into registry entries. If the option is a single tag, that tag is returned as is.
     * Otherwise, a plain set of all the entries is returned, with tags replaced by their contents.
     *
     * Invalid entries are skipped and only logged when [net.justmili.mcl.config.Config.enableDebug] is enabled.
     *
     * A new set is built on each call. Only call it after tags have loaded, as unloaded tags are treated as missing.
     * Call again after datapack reload to update set entries if anything changed.
     *
     * @return the set of matching entries, empty if nothing could be found
     */
    fun getHolderSet(): HolderSet<E> {
        val getter: HolderGetter<E> = /*? if >= 1.21.11 {*//*registry*//*?} else {*/registry.asLookup()/*?}*/
        val entries = get()

        // For entries with only a single tag return its own set instead of copying it
        if (entries.size == 1 && entries[0].tag()) {
            val tag = TagKey.create(registry.key(), entries[0].id())
            val named = getter.get(tag).orElse(null)
            if (named != null) return named
            MConfigLogger.missingHolder(this, "tag", entries[0].id().toString())
            return HolderSet.direct(emptyList<Holder<E>>())
        }

        // Everything else goes into one set. Duplicates are dropped
        val holders = LinkedHashSet<Holder<E>>(entries.size * 2)
        for (entry in entries) {
            if (entry.tag()) {
                // Add entries from tag
                val named = getter.get(TagKey.create(registry.key(), entry.id())).orElse(null)
                if (named != null) named.forEach { holders.add(it) } else MConfigLogger.missingHolder(this, "tag", entry.id().toString())
            } else {
                // Add entry
                val holder = getter.get(ResourceKey.create(registry.key(), entry.id())).orElse(null)
                if (holder != null) holders.add(holder) else MConfigLogger.missingHolder(this, "element", entry.id().toString())
            }
        }
        return HolderSet.direct(holders.toList())
    }
}