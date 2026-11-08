package net.justmili.mcl.v2.lib.config

import net.justmili.mcl.v1.lib.util.Platform
import net.justmili.mcl.v2.lib.config.converter.OldConfigLoader
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.serializers.*
import java.nio.file.Files
import java.nio.file.Path

class MConfigLoader(modId: String, customFileName: String?, configType: ConfigTypes, fileType: FileTypes, createSubDirectory: Boolean) {
    val path: Path
    private val root = CategoryItem("root", null)
    private val serializer = getSerializer(fileType)
    private var convertLoader: OldConfigLoader? = null

    init {
        val configDir = Platform.CONFIG_DIR
        val directory = if (createSubDirectory) configDir.resolve(modId) else configDir
        val extension = fileType.extension
        val usesSuffix = customFileName != null || !createSubDirectory
        val fileName = customFileName ?: modId

        // Uses alternative suffix for Common Priority config files if a regular common config exists
        val primaryPath = getPath(directory, fileName, configType.suffix, extension, usesSuffix)
        val alternativePath = getPath(directory, fileName, configType.altSuffix, extension, usesSuffix)
        val usesAltSuffix = CLAIMED_PATHS.contains(primaryPath) && primaryPath != alternativePath
        this.path = if (usesAltSuffix) alternativePath else primaryPath

        if (!CLAIMED_PATHS.add(path)) MConfigLogger.pathInUse(path)
    }

    fun getRoot(): CategoryItem {
        return root
    }

    internal fun addForConversion(oldPath: Path, deleteOldFile: Boolean) {
        val oldConfigs = convertLoader ?: OldConfigLoader().also { this.convertLoader = it }
        oldConfigs.add(oldPath, deleteOldFile)
    }

    @Synchronized fun buildOrLoad() {
        serializer.validate(path, root)
        if (Files.exists(path)) {
            if (serializer.load(path, root)) save()
        } else {
            convertLoader?.importInto(root)
            if (serializer.write(path, root)) convertLoader?.deleteConverted()
        }
        convertLoader = null
    }

    @Synchronized fun save() {
        if (!serializer.write(path, root)) MConfigLogger.writeFail(path)
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