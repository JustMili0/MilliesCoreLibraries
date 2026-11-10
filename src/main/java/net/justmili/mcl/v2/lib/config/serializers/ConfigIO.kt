package net.justmili.mcl.v2.lib.config.serializers

import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.options.ConfigOption
import net.justmili.mcl.v2.lib.config.options.OptionFlags
import java.nio.file.Path

interface ConfigIO {

    fun write(path: Path, root: CategoryItem): Boolean

    fun load(path: Path, root: CategoryItem): Boolean

    fun validate(path: Path, root: CategoryItem) {}

    companion object {
        const val TOKEN_SLASH: String = "// "
        const val TOKEN_POUND: String = "# "
        const val TOKEN_KEY: String = "_c"
        const val INDENT: String = "  "

        inline fun eachCommentLine(comment: String?, action: (String, Int, Int) -> Unit) {
            val text = addPeriod(comment) ?: return
            var start = 0
            while (true) {
                val end = text.indexOf('\n', start)
                if (end < 0) {
                    action(text, start, text.length)
                    return
                }
                action(text, start, end)
                start = end + 1
            }
        }

        inline fun eachCommentLine(option: ConfigOption<*>, action: (String, Int, Int) -> Unit) {
            eachCommentLine(option.comment, action)
            if (option.flagMask != 0) {
                val line = getFlagsLine(option)
                action(line, 0, line.length)
            }
            val line = getDefaultsLine(option)
            action(line, 0, line.length)
        }

        fun addPeriod(comment: String?): String? {
            if (comment.isNullOrBlank()) return null
            val stripped = comment.trimEnd()
            return if (stripped.last() in ".!?:;*-~") stripped else "$stripped."
        }

        fun getFlagsLine(option: ConfigOption<*>): String {
            val line = StringBuilder("Requires: ")
            var first = true
            for (flag in OptionFlags.entries) {
                if (!option.hasFlag(flag)) continue
                if (!first) line.append(", ")
                line.append(flag.descName)
                first = false
            }
            return line.toString()
        }

        fun getDefaultsLine(option: ConfigOption<*>): String {
            val defaultsBase = option.getDefaultsComment().replace("\r", "\\r").replace("\n", "\\n")
            val defaultsFormatted = if (defaultsBase.isEmpty()) "Empty by default" else "Default: $defaultsBase"
            val bounds = option.bounds
            return if (bounds == null) defaultsFormatted else "$bounds - $defaultsFormatted"
        }
    }
}