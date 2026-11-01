package net.justmili.mcl.v2.lib.config

import net.justmili.mcl.CoreLibs
import net.justmili.mcl.v1.lib.util.Platform
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.writers.FileWriter
import net.justmili.mcl.v2.lib.config.writers.json.Json5IO
import net.justmili.mcl.v2.lib.config.writers.json.JsonIO
import net.justmili.mcl.v2.lib.config.writers.properties.PropertiesIO
import java.io.File
import java.nio.file.Path

class MConfigLoader(private val modId: String, customFileName: String?, configType: ConfigTypes, fileType: FileTypes, createSubDirectory: Boolean) {
    private val root = CategoryItem("root", null)
    private val fileName = customFileName ?: modId
    private val writer = getWriter(fileType)
    private val suffix: String
    private val path: Path
    private val file: File

    init {
        val configDir = Platform.CONFIG_DIR
        val directory = if (createSubDirectory) configDir.resolve(modId) else configDir
        val extension = fileType.getExtension()
        val usesSuffix = customFileName != null || !createSubDirectory

        // Uses alternative suffix for Common Priority config files if a regular common config exists
        val primaryPath = getPath(directory, configType.getSuffix(), extension, usesSuffix)
        val alternativePath = getPath(directory, configType.getAltSuffix(), extension, usesSuffix)
        val usesAltSuffix = CLAIMED_PATHS.contains(primaryPath) && primaryPath != alternativePath
        this.suffix = if (usesAltSuffix) configType.getAltSuffix() else configType.getSuffix()
        this.path = if (usesAltSuffix) alternativePath else primaryPath
        this.file = this.path.toFile()

        if (!CLAIMED_PATHS.add(path)) CoreLibs.LOGGER.warn("Config file name '{}' of '{}' is already in use, files will overwrite each other.", file.name, modId)
    }

    fun getRoot(): CategoryItem {
        return root
    }

    @Synchronized
    fun save() {
        if (!file.exists()) CoreLibs.LOGGER.error("Config file '{}' of '{}' is missing, creating new.", file.name, modId)
        writer.write(path, root)
    }

    @Synchronized
    fun buildOrLoad() {
        if (!file.exists()) {
            writer.write(path, root)
        } else if (writer.load(path, root)) {
            save()
        } else {
            CoreLibs.LOGGER.error("Could not read config of '{}' ({}).", modId, file.name)
        }
    }

    private fun getWriter(fileType: FileTypes): FileWriter {
        return when (fileType) {
            FileTypes.JSON -> JsonIO()
            FileTypes.JSON5 -> Json5IO()
            FileTypes.PROPERTIES -> PropertiesIO()
            FileTypes.TOML -> throw UnsupportedOperationException("TOML support is not yet fully implemented.")
        }
    }

    private fun getPath(directory: Path, suffix: String, extension: String, usesSuffix: Boolean): Path {
        val file = if (usesSuffix) "$fileName-$suffix" else suffix
        return directory.resolve(file + extension)
    }

    companion object {
        private val CLAIMED_PATHS: MutableSet<Path> = HashSet()

        @JvmStatic
        fun loadError(e: String?) {
            CoreLibs.LOGGER.error("Failed to load config: {}", e)
        }
    }
}