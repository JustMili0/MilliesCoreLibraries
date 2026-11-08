package net.justmili.mcl.v2.lib.config.options

import com.google.gson.*
import com.mojang.serialization.*
import net.justmili.mcl.v2.lib.config.MConfigLoader
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.items.ConfigItem
import net.minecraft.resources.ResourceLocation
import java.nio.file.Path
import java.util.*

class ConfigOption<T : Any>(val key: String, val default: T, private val codec: Codec<T>, private val limit: OptionBounds<in T>?, private val loader: MConfigLoader, val flagMask: Int) : ConfigItem {
    @Volatile private var value: T = default
    @Volatile private var synced: T? = null
    var attachedImage: ResourceLocation? = null
    var comment: String? = null
    var oldKey: String? = null
    val path: Path get() = loader.path
    val bounds: String? get() = limit?.describe()

    fun get(): T {
        return synced ?: value
    }

    fun set(value: T): Boolean {
        if (!isValid(value)) return false
        if (this.value == value) return true

        this.value = value
        loader.save()
        return true
    }

    fun setSynced(value: T): Boolean {
        if (!isValid(value)) return false

        this.synced = value
        return true
    }

    fun clearSynced() {
        this.synced = null
    }

    fun isSynced(): Boolean {
        return synced != null
    }

    fun hasFlag(flag: OptionFlags): Boolean {
        return (flagMask and flag.mask) != 0
    }

    fun <R> encode(ops: DynamicOps<R>): Optional<R> {
        return codec.encodeStart(ops, value).resultOrPartial { error ->
            MConfigLogger.encodeFail(this, error)
        }
    }

    fun <R> load(ops: DynamicOps<R>, input: R): Boolean {
        val decoded = codec.parse(ops, input).result().orElse(null) ?: return false
        if (!isValid(decoded)) return false

        this.value = decoded
        return true
    }

    fun toText(): String {
        val element = encode(JsonOps.INSTANCE).orElse(null) ?: return ""
        return if (element is JsonPrimitive) element.asString else element.toString()
    }

    fun loadText(raw: String): Boolean {
        return load(JsonOps.INSTANCE, when (default) {
            is String -> JsonPrimitive(raw)
            is Enum<*> -> JsonPrimitive(raw.trim())
            else -> parseText(raw)
        })
    }

    private fun isValid(value: T): Boolean {
        return limit == null || limit.isValid(value)
    }

    private fun parseText(raw: String): JsonElement {
        return try {
            JsonParser.parseString(raw.trim())
        } catch (e: JsonParseException) {
            JsonPrimitive(raw)
        }
    }
}