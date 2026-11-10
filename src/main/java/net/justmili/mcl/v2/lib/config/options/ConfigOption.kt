package net.justmili.mcl.v2.lib.config.options

import com.google.gson.*
import com.mojang.serialization.*
import net.justmili.mcl.v2.lib.config.MConfigLoader
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.items.ConfigItem
import net.minecraft.resources.ResourceLocation
import java.nio.file.Path
import java.util.*

open class ConfigOption<O : Any>(val key: String, val default: O, private val codec: Codec<O>, private val limit: OptionBounds<in O>?, private val loader: MConfigLoader, val flagMask: Int, private val isText: Boolean) : ConfigItem {
    @Volatile protected var value: O = default
        private set
    @Volatile private var synced: O? = null
    var attachedImage: ResourceLocation? = null
    var comment: String? = null
    var oldKey: String? = null
    val path: Path get() = loader.path
    val bounds: String? get() = limit?.describe()

    fun get() = synced ?: value

    open fun set(value: O): Boolean {
        if (!isValid(value)) return false
        if (this.value == value) return true

        this.value = value
        loader.tryWrite()
        return true
    }

    open fun setSynced(value: O): Boolean {
        if (!isValid(value)) return false

        this.synced = value
        return true
    }

    fun clearSynced() {
        this.synced = null
    }

    fun isSynced() = synced != null

    fun hasFlag(flag: OptionFlags) =  (flagMask and flag.mask) != 0

    fun <R> encode(ops: DynamicOps<R>): Optional<R> = codec.encodeStart(ops, value).resultOrPartial { error -> MConfigLogger.encodeFail(this, error) }

    fun <R> load(ops: DynamicOps<R>, input: R): Boolean {
        val decoded = codec.parse(ops, input).result().orElse(null) ?: return false
        if (!isValid(decoded)) return false

        this.value = decoded
        return true
    }

    fun toText() = textOf(value)

    private fun textOf(value: O): String {
        val element = codec.encodeStart(JsonOps.INSTANCE, value).resultOrPartial { error -> MConfigLogger.encodeFail(this, error) }.orElse(null) ?: return ""
        return if (element is JsonPrimitive) element.asString else element.toString()
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

    private fun isValid(value: O) = limit == null || limit.isValid(value)

    private fun parseText(raw: String): JsonElement {
        return try {
            JsonParser.parseString(raw.trim())
        } catch (e: JsonParseException) {
            JsonPrimitive(raw)
        }
    }
}