package net.justmili.mcl.v2.lib.config

import net.justmili.mcl.v1.lib.util.Platform
import net.justmili.mcl.v2.lib.config.options.ConfigOption
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

internal object MConfigLogger {
    var LOGGER: Logger = LoggerFactory.getLogger(MConfigLoader::class.java)

    fun readFail(path: Path, reason: String? = "unknown error") = LOGGER.error("Could not read from file '{}': {}", getName(path), reason)
    fun writeFail(path: Path, reason: String? = "unknown error") = LOGGER.error("Could not write to file '{}': {}", getName(path), reason)
    fun deleteFail(path: Path, reason: String? = "unknown error") = dev("Could not delete file '{}': {}", getName(path), reason)
    fun encodeFail(option: ConfigOption<*>, reason: String? = "unknown error") = dev("Could not encode option '{}' of '{}': {}", option.key, getName(option.path), reason)
    fun decodeFail(option: ConfigOption<*>, reason: String? = "unknown error") = dev("Could not decode option '{}' of '{}': {}", option.key, getName(option.path), reason)
    fun invalidValue(option: ConfigOption<*>, key: String? = option.key) = LOGGER.warn("Invalid value for option '{}' in '{}'", key, getName(option.path))
    fun unsupportedElement(path: Path, element: String = "unknown element", reason: String = "unknown error") = dev("Unsupported element(s) '{}' in file '{}': {}", element, getName(path), reason)
    fun pathInUse(path: Path) = dev("File name '{}' already in use by a different file. They will overwrite eachother.", getName(path))

    private fun dev(message: String, vararg arguments: Any?) = if (Platform.IS_DEV_ENV) LOGGER.warn(message, *arguments) else {
    }

    private fun getName(path: Path): Path = Platform.CONFIG_DIR.relativize(path)
}
