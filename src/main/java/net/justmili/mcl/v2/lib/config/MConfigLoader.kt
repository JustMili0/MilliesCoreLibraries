package net.justmili.mcl.v2.lib.config

import net.justmili.mcl.v1.lib.util.Platform
import net.justmili.mcl.v2.lib.config.converter.OldConfigLoader
import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.serializers.*
import net.justmili.mcl.v2.lib.config.sync.MConfigSync
import net.justmili.mcl.v2.lib.config.sync.SyncPriorityConfigs
import net.minecraft.server.MinecraftServer
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.nameWithoutExtension

class MConfigLoader(modId: String, customFileName: String?, configType: ConfigTypes, fileType: FileTypes, createSubDirectory: Boolean) {
    val path: Path
    val sync: MConfigSync?
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

        // The id is the same on both sides as long as both have the same mod and file name
        this.sync = if (configType == ConfigTypes.COMMON_SERVER_PRIORITY) MConfigSync("$modId:${path.nameWithoutExtension}", root, path).also { SyncPriorityConfigs.register(it) } else null
    }

    internal fun addForConversion(oldPath: Path, deleteOldFile: Boolean) {
        val oldConfigs = convertLoader ?: OldConfigLoader().also { this.convertLoader = it }
        oldConfigs.add(oldPath, deleteOldFile)
    }

    internal fun tryWrite(): Boolean {
        synchronized(path) {
            // FileIO write logs write issues, tryWrite() doesn't have to
            return serializer.write(path, root)
        }
    }

    internal fun tryRead(): Boolean {
        synchronized(path) {
            // Serializers log read issues, tryRead() doesn't have to
            return serializer.read(path, root)
        }
    }

    fun reload(server: MinecraftServer?): Boolean {
        val read = synchronized(path) {
            if (path.exists()) tryRead() else {
                tryWrite()
                false
            }
        }
        // Players on the server get updated values
        if (read) sync?.let { SyncPriorityConfigs.sendToAll(server, it) }
        return read
    }

    internal fun buildOrLoad() {
        synchronized(path) {
            serializer.validate(path, root)
            if (path.exists()) {
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
            FileTypes.TOML -> TomlIO
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