package net.justmili.mcl.v2.lib.config.converter

import com.google.gson.*
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.serializers.SharedPropertiesIO
import net.justmili.mcl.v2.lib.util.common.JsonSerializer
import java.io.IOException
import java.lang.ref.SoftReference
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.deleteIfExists
import kotlin.io.path.isRegularFile

class OldConfigConverter(val path: Path, val deleteAfter: Boolean) {
    private var finder: ((String) -> JsonElement?)? = null

    internal fun read(): Boolean {
        CACHE[path]?.get()?.let {
            finder = it
            return true
        }
        if (!path.isRegularFile()) return false

        // Check for whether the file has a supported format in this config lib
        val success = when (path.fileName.toString().substringAfterLast('.', "").lowercase()) {
            "json", "json5" -> readJson()
            "properties" -> readProperties()
            else -> fail("unsupported file format for conversion")
        }
        finder?.let { CACHE[path] = SoftReference(it) }
        return success
    }

    internal fun find(key: String) = finder?.invoke(key)

    internal fun delete() {
        try {
            path.deleteIfExists()
        } catch (e: IOException) {
            MConfigLogger.deleteFail(path, e.message)
        }
    }

    private fun readJson(): Boolean {
        val root = JsonSerializer.readTree(path) { reason -> fail(reason) }.orElse(null) ?: return false
        if (root !is JsonObject) return fail("the file doesn't contain a JSON object")
        finder = { key -> getElement(root, key) }
        return true
    }

    private fun readProperties(): Boolean {
        val properties = SharedPropertiesIO.read(path) { reason -> fail(reason) } ?: return false
        finder = { key -> properties.getProperty(key)?.let { JsonPrimitive(it) } }
        return true
    }

    // Matches the key literally first, then as a path through nested objects ("category.key")
    private fun getElement(obj: JsonObject, key: String): JsonElement? {
        obj.get(key)?.let { return if (it.isJsonNull) null else it }

        var dot = key.indexOf('.')
        while (dot != -1) {
            val child = obj.get(key.substring(0, dot)) as? JsonObject
            if (child != null) getElement(child, key.substring(dot + 1))?.let { return it }
            dot = key.indexOf('.', dot + 1)
        }
        return null
    }

    private fun fail(reason: String?): Boolean {
        if (MConfigLogger.IS_DEBUG) MConfigLogger.readFail(path, reason)
        return false
    }

    companion object {
        private val CACHE: MutableMap<Path, SoftReference<(String) -> JsonElement?>> = ConcurrentHashMap()
    }
}