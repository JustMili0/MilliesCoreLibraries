package net.justmili.mcl.v2.lib.util.common

import com.google.gson.*
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import net.justmili.mcl.CoreLibs
import java.nio.file.Path
import java.util.*
import java.util.function.Consumer
import kotlin.io.path.exists

/**
 * Generic serializer for writing/reading JSON files.
 */
object JsonSerializer {
    private val GSON = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    @JvmStatic
    fun <T> write(path: Path, codec: Codec<T>, value: T): Boolean {
        val json = codec.encodeStart(JsonOps.INSTANCE, value)
            .resultOrPartial { e -> fail(path, "encode JSON for", e) }
            .orElse(null) ?: return false
        return FileIO.write(path) { writer ->
            GSON.toJson(json, writer)
            writer.write("\n")
        }
    }

    /**
     * Reads file on path with the specified codec.
     * Partial results are kept by default, can be turned off.
     * @param exception called with every error message, logs them by default
     * @return the parsed value, empty if the file doesn't exist or nothing could be parsed
     */
    @JvmStatic
    @JvmOverloads
    fun <T : Any> read(path: Path, codec: Codec<T>, exception: Consumer<String?> = Consumer { msg -> fail(path, "read", msg) }, keepPartial: Boolean = true): Optional<T> {
        return readTree(path, exception).map { json ->
            val result = codec.parse(JsonOps.INSTANCE, json)
            if (keepPartial) {
                // If keep partial, give full result or partial if something fails...
                result.resultOrPartial { e -> exception.accept(e) }.orElse(null)
            } else {
                // ...Otherwise, return result or null
                result.error().ifPresent { e -> exception.accept(e.message()) }
                result.result().orElse(null)
            }
        }
    }

    /**
     * Reads file on path as raw JSON tree with no specific codec(s).
     * @param exception called with the error message, logs it by default
     * @return the parsed tree, empty if the file doesn't exist or can't be read
     */
    @JvmStatic
    @JvmOverloads
    fun readTree(path: Path, exception: Consumer<String?> = Consumer { msg -> fail(path, "read", msg) }): Optional<JsonElement> {
        if (!path.exists()) return Optional.empty()
        return Optional.ofNullable(FileIO.read(path, { msg -> exception.accept(msg) }) { reader -> JsonParser.parseReader(reader) })
    }

    internal fun fail(path: Path, action: String, message: String?): Boolean {
        CoreLibs.LOGGER.error("Could not $action '{}': {}", path, message)
        return false
    }
}