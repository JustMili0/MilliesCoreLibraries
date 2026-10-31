package net.justmili.mcl.v2.lib.config

import net.justmili.mcl.CoreLibs
import net.justmili.mcl.v1.lib.util.Platform
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.writers.FileWriter
import net.justmili.mcl.v2.lib.config.writers.json.Json5IO
import net.justmili.mcl.v2.lib.config.writers.json.JsonIO
import net.justmili.mcl.v2.lib.config.writers.properties.PropertiesIO
import java.io.IOException
import java.nio.file.*

class MConfigLoader(private val modId: String, customFileName: String?, configType: ConfigTypes, fileType: FileTypes, createSubDirectory: Boolean) {
    private val root = CategoryItem("root", null)
    private val fileName = customFileName ?: modId
    private val writer = getWriter(fileType)
    private val suffix: String
    private val path: Path

    init {
        val configDir = Platform.CONFIG_DIR

        if (configType == ConfigTypes.COMMON_CLIENT_PRIORITY) {
            // Client -> Server
        } else if (configType == ConfigTypes.COMMON_SERVER_PRIORITY) {
            // Server -> Client
        }

        val directory = if (createSubDirectory) configDir.resolve(modId) else configDir
        val extension = fileType.extension()
        val fileNameWithSuffix = customFileName != null || !createSubDirectory

        val primaryPath = getPath(directory, configType.suffix(), extension, fileNameWithSuffix)
        val alternativePath = getPath(directory, configType.altSuffix(), extension, fileNameWithSuffix)

        // Uses alternative suffix for Common Priority config files if a regular common config exists
        val usesAltSuffix = CLAIMED_PATHS.contains(primaryPath) && primaryPath != alternativePath
        this.suffix = if (usesAltSuffix) configType.altSuffix() else configType.suffix()
        this.path = if (usesAltSuffix) alternativePath else primaryPath

        if (!CLAIMED_PATHS.add(path)) CoreLibs.LOGGER.warn("Config file '{}' of '{}' is already used by another config, they will overwrite each other.", path, modId)
    }

    fun root(): CategoryItem {
        return root
    }

    @Synchronized
    fun save() {
        writer.write(path, root)
    }

    @Synchronized
    fun buildOrLoad() {
        if (!path.toFile().exists()) {
            save()
            return
        }
        if (!writer.load(path, root)) backupIfBroken()
        save()
    }

    private fun backupIfBroken() {
        val backupPath = path.resolveSibling(path.fileName.toString() + ".mclb") // MCLB - MConfigLoader Backup
        try {
            Files.move(path, backupPath, StandardCopyOption.REPLACE_EXISTING)
            CoreLibs.LOGGER.warn("Could not read config of '{}' ({}, {}). File has been backed up to {}.", modId, path, suffix, backupPath)
        } catch (e: IOException) {
            CoreLibs.LOGGER.error("Failed to back up unreadable config of '{}' ({}): {}", modId, suffix, e.message)
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

    private fun getPath(directory: Path, suffix: String, extension: String, fileNameWithSuffix: Boolean): Path {
        val file = if (fileNameWithSuffix) "$fileName-$suffix" else suffix
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