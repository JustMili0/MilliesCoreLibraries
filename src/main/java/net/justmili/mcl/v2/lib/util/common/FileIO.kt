package net.justmili.mcl.v2.lib.util.common

import net.justmili.mcl.CoreLibs
import java.io.*
import java.nio.file.*
import kotlin.io.path.bufferedReader
import kotlin.io.path.bufferedWriter

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
            if (parent != null) Files.createDirectories(parent)

            bufferedWriter(path).use { writer -> writing.accept(writer) }
            return true
        } catch (e: IOException) {
            CoreLibs.LOGGER.error("Failed to write '{}': {}", path, e.message)
            return false
        }
    }

    @JvmStatic
    fun bufferedReader(path: Path): BufferedReader = path.bufferedReader(options = READ_ONLY)

    @JvmStatic
    fun bufferedWriter(path: Path): BufferedWriter = path.bufferedWriter(options = WRITE_AND_CREATE)
}