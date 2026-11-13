package net.justmili.mcl.v2.lib.config

import net.justmili.mcl.config.Config
import net.justmili.mcl.v1.lib.util.Platform
import net.justmili.mcl.v2.lib.config.options.ConfigOption
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

internal object MConfigLogger {
    var LOGGER: Logger = LoggerFactory.getLogger(MConfigLoader::class.java)
    val IS_DEBUG: Boolean get() = Platform.IS_DEV_ENV || Config.enableDebug?.get() == true

    fun readFail(path: Path, reason: String? = "invalid") = LOGGER.error("Could not read from file '{}': {}", getName(path), reason)
    fun writeFail(path: Path, reason: String? = "invalid") = LOGGER.error("Could not write to file '{}': {}", getName(path), reason)
    fun deleteFail(path: Path, reason: String? = "invalid") = dev("Could not delete file '{}': {}", getName(path), reason)
    fun encodeFail(option: ConfigOption<*>, reason: String) = dev("Could not encode option '{}' of '{}': {}", option.key, getName(option.path), reason)
    fun invalidValue(option: ConfigOption<*>, key: String? = option.key) = LOGGER.warn("Invalid value for option '{}' in '{}'", key, getName(option.path))
    fun unsupportedElement(path: Path, element: String, reason: String) = dev("Unsupported element(s) '{}' in file '{}': {}", element, getName(path), reason)
    fun pathInUse(path: Path) = dev("File name '{}' already in use by a different file. They will overwrite eachother.", getName(path))

    private fun dev(message: String, vararg arguments: Any?) = if (IS_DEBUG) LOGGER.warn(message, *arguments) else {
    }

    private fun getName(path: Path): Path = Platform.CONFIG_DIR.relativize(path)
}
