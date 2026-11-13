package net.justmili.mcl.v2.lib.config

import net.justmili.mcl.v1.lib.util.Platform
import net.justmili.mcl.v2.lib.config.converter.OldConfigLoader
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.serializers.*
import java.nio.file.Files
import java.nio.file.Path

class MConfigLoader(modId: String, customFileName: String?, configType: ConfigTypes, fileType: FileTypes, createSubDirectory: Boolean) {
    val path: Path
    val root = CategoryItem("root", null)
    private val serializer = getSerializer(fileType)
    private var convertLoader: OldConfigLoader? = null

    init {
        val configDir = Platform.CONFIG_DIR
        val directory = if (createSubDirectory) configDir.resolve(modId) else configDir
        val usesSuffix = customFileName != null || !createSubDirectory
        val fileName = customFileName ?: modId

        this.path = getPath(directory, fileName, configType.type, fileType.extension, usesSuffix)
        if (!CLAIMED_PATHS.add(path)) MConfigLogger.pathInUse(path)
    }

    internal fun addForConversion(oldPath: Path, deleteOldFile: Boolean) {
        val oldConfigs = convertLoader ?: OldConfigLoader().also { this.convertLoader = it }
        oldConfigs.add(oldPath, deleteOldFile)
    }

    internal fun tryWrite(): Boolean {
        synchronized(path) {
            if (serializer.write(path, root)) return true
            MConfigLogger.writeFail(path, "unknown") // TODO: make it give a proper error message
            return false
        }
    }

    internal fun tryRead(): Boolean {
        synchronized(path) {
            // Serializers log read issues, tryRead() doesn't have to
            return serializer.load(path, root)
        }
    }

    fun reload(): Boolean {
        synchronized(path) {
            if (Files.exists(path)) return tryRead()
            tryWrite()
            return false
        }
    }

    internal fun buildOrLoad() {
        synchronized(path) {
            serializer.validate(path, root)
            if (Files.exists(path)) {
                if (tryRead()) tryWrite()
            } else {
                convertLoader?.importInto(root)
                if (tryWrite()) convertLoader?.deleteConverted()
            }
            convertLoader = null
        }
    }

    private fun getSerializer(fileType: FileTypes): ConfigIO {
        return when (fileType) {
            FileTypes.JSON -> JsonIO
            FileTypes.JSON5 -> Json5IO
            FileTypes.PROPERTIES -> PropertiesIO
            FileTypes.TOML -> throw UnsupportedOperationException("TOML support is not yet fully implemented.")
        }
    }

    private fun getPath(directory: Path, fileName: String, suffix: String, extension: String, usesSuffix: Boolean): Path {
        val file = if (usesSuffix) "$fileName-$suffix" else suffix
        return directory.resolve(file + extension)
    }

    companion object {
        private val CLAIMED_PATHS: MutableSet<Path> = HashSet()
    }
}