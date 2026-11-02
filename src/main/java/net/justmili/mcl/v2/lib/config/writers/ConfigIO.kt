package net.justmili.mcl.v2.lib.config.writers

import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.options.ConfigOption
import java.io.BufferedWriter
import java.io.IOException
import java.nio.file.Path

interface ConfigIO {

    fun write(path: Path, root: CategoryItem)

    fun load(path: Path, root: CategoryItem): Boolean

    fun getDetails(option: ConfigOption<*>): List<String> {
        val lines = ArrayList<String>()
        if (option.flags.isNotEmpty()) lines.add("Requires: " + getFlagNames(option))

        val parts = ArrayList<String>()
        option.bounds?.let(parts::add)

        val default = option.default.toString().replace("\r", "\\r").replace("\n", "\\n")
        parts.add(if (default.isEmpty()) "Empty by default" else "Default: $default")
        lines.add(parts.joinToString(" - "))

        return lines
    }

    fun addPeriod(comment: String?): String? {
        if (comment.isNullOrBlank()) return null
        val stripped = comment.trimEnd()
        return if (stripped.last() in ".!?:;*-~") stripped else "$stripped."
    }

    @Throws(IOException::class)
    fun writeDescComment(writer: BufferedWriter, comment: String?, token: String, indent: String) {
        val text = addPeriod(comment) ?: return
        for (line in text.split("\n")) {
            writer.write(indent + token + line + "\n")
        }
    }

    @Throws(IOException::class)
    fun writeOptionComment(writer: BufferedWriter, option: ConfigOption<*>, token: String, indent: String) {
        writeDescComment(writer, option.comment, token, indent)
        for (line in getDetails(option)) {
            writer.write(indent + token + line + "\n")
        }
    }

    private fun getFlagNames(option: ConfigOption<*>): String {
        return option.flags.joinToString(", ") {it.getDescName()}
    }

    companion object {
        const val TOKEN_SLASH: String = "// "
        const val TOKEN_POUND: String = "# "
        const val TOKEN_KEY: String = "_c"
        const val NO_INDENT: String = ""
        const val INDENT: String = "  "
    }
}