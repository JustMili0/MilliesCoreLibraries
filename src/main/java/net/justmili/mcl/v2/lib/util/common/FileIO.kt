package net.justmili.mcl.v2.lib.util.common

import com.google.gson.JsonParseException
import net.justmili.mcl.CoreLibs
import java.io.*
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.*

object FileIO {

    // Read-only
    @JvmField val READ_ONLY = arrayOf(StandardOpenOption.READ)

    // Read, create the file if it doesn't exist
    @JvmField val READ_AND_CREATE = arrayOf(StandardOpenOption.CREATE, StandardOpenOption.READ)

    // Write, also change the file length so if the file is shorter the file doesn't become invalid. yes that's a thing.
    @JvmField val WRITE_ONLY = arrayOf(StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)

    // Create the file if it doesn't exist, then above
    @JvmField val WRITE_AND_CREATE = arrayOf(StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)

    fun interface Writing {
        @Throws(IOException::class) fun accept(writer: BufferedWriter)
    }

    @JvmStatic
    fun write(path: Path, writing: Writing): Boolean {
        try {
            val parent = path.toAbsolutePath().parent
            if (parent != null) parent.createDirectories()

            bufferedWriter(path).use { writer -> writing.accept(writer) }
            return true
        } catch (e: IOException) {
            CoreLibs.LOGGER.error("Failed to write '{}': {}", path, e.message)
            return false
        }
    }

    @JvmStatic
    fun <T : Any> read(path: Path, exception: (String?) -> Unit, reader: (BufferedReader) -> T): T? {
        return try {
            path.bufferedReader(options = READ_ONLY).use(reader)
        } catch (e: IOException) {
            exception(e.message)
            null
        } catch (e: JsonParseException) {
            exception(e.message)
            null
        } catch (e: IllegalArgumentException) {
            exception(e.message)
            null
        }
    }

    @JvmStatic
    fun bufferedReader(path: Path): BufferedReader = path.bufferedReader(options = READ_ONLY)

    @JvmStatic
    fun bufferedWriter(path: Path): BufferedWriter = path.bufferedWriter(options = WRITE_AND_CREATE)
}