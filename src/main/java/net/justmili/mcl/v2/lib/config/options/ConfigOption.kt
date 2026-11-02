package net.justmili.mcl.v2.lib.config.options

import com.google.gson.*
import com.mojang.serialization.*
import net.justmili.mcl.CoreLibs
import net.justmili.mcl.v2.lib.config.items.ConfigItem
import net.minecraft.resources.ResourceLocation
import java.util.*
import java.util.function.Predicate

class ConfigOption<T : Any>(val key: String, val default: T, private val codec: Codec<T>, private val validator: Predicate<T>, val bounds: String?, private val saver: Runnable, vararg flags: OptionFlags) : ConfigItem {
    @Volatile private var value: T = default
    @Volatile private var synced: T? = null
    val flags: Set<OptionFlags> = Collections.unmodifiableSet(flags.toCollection(EnumSet.noneOf(OptionFlags::class.java)))
    var attachedImage: ResourceLocation? = null
    var comment: String? = null

    fun get(): T {
        return synced ?: value
    }

    fun set(value: T): Boolean {
        if (!isValid(value)) return false
        if (this.value == value) return true

        this.value = value
        saver.run()
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

    fun <R> encode(ops: DynamicOps<R>): Optional<R> {
        return codec.encodeStart(ops, value).resultOrPartial {error -> CoreLibs.LOGGER.warn("Failed to encode config option '{}': {}", key, error)}
    }

    fun <R> load(ops: DynamicOps<R>, input: R): Boolean {
        val decoded = codec.parse(ops, input).result().filter(validator).orElse(null) ?: return false

        this.value = decoded
        return true
    }

    fun toText(): String {
        val element = encode(JsonOps.INSTANCE).orElse(null) ?: return ""
        return if (element is JsonPrimitive) element.asString else element.toString()
    }

    fun loadText(raw: String): Boolean {
        return load(JsonOps.INSTANCE, if (default is String) JsonPrimitive(raw) else parseText(raw))
    }

    private fun isValid(value: T): Boolean {
        return validator.test(value)
    }

    private fun parseText(raw: String): JsonElement {
        return try {
            JsonParser.parseString(raw.trim())
        } catch (e: JsonParseException) {
            JsonPrimitive(raw)
        }
    }
}