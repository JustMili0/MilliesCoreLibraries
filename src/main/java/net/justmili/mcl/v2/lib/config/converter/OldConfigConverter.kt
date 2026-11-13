package net.justmili.mcl.v2.lib.config.converter

import com.google.gson.*
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.util.common.FileIO
import java.io.IOException
import java.lang.ref.SoftReference
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class OldConfigConverter(val path: Path, val deleteAfter: Boolean) {
    private var finder: ((String) -> JsonElement?)? = null

    fun read(): Boolean {
        CACHE[path]?.get()?.let {
            finder = it
            return true
        }
        if (!Files.isRegularFile(path)) return false

        val success = when (path.fileName.toString().substringAfterLast('.', "").lowercase()) {
            "json", "json5" -> readJson()
            "properties" -> readProperties()
            else -> fail("unsupported file format for conversion")
        }
        finder?.let { CACHE[path] = SoftReference(it) }
        return success
    }

    fun find(key: String) = finder?.invoke(key)

    fun delete() {
        try {
            Files.deleteIfExists(path)
        } catch (e: IOException) {
            MConfigLogger.deleteFail(path, e.message)
        }
    }

    private fun readJson(): Boolean {
        val root = try {
            FileIO.bufferedReader(path).use { reader -> JsonParser.parseReader(reader) }
        } catch (e: IOException) {
            return fail(e.message)
        } catch (e: JsonParseException) {
            return fail(e.message)
        }

        if (root !is JsonObject) return fail("the file doesn't contain a JSON object")
        finder = { key -> resolve(root, key) }
        return true
    }

    private fun readProperties(): Boolean {
        val properties = Properties()
        try {
            FileIO.bufferedReader(path).use { reader -> properties.load(reader) }
        } catch (e: IOException) {
            return fail(e.message)
        } catch (e: IllegalArgumentException) {
            return fail(e.message)
        }

        finder = { key -> properties.getProperty(key)?.let { JsonPrimitive(it) } }
        return true
    }

    // Matches the key literally first, then as a path through nested objects ("category.key")
    private fun resolve(obj: JsonObject, key: String): JsonElement? {
        obj.get(key)?.let { return if (it.isJsonNull) null else it }

        var dot = key.indexOf('.')
        while (dot != -1) {
            val child = obj.get(key.substring(0, dot)) as? JsonObject
            if (child != null) resolve(child, key.substring(dot + 1))?.let { return it }
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