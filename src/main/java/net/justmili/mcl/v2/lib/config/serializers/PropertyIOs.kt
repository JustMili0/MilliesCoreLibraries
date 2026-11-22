package net.justmili.mcl.v2.lib.config.serializers

import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.items.*
import net.justmili.mcl.v2.lib.config.options.ConfigOption
import net.justmili.mcl.v2.lib.util.common.FileIO
import java.io.Writer
import java.nio.file.Path
import java.util.*

internal object PropertiesIO : ConfigIO {

    override fun validate(path: Path, root: CategoryItem) {
        val keys = HashSet<String>()
        for (option in root.getChildren()) {
            require(keys.add(option.key)) {
                "Duplicate config option '${option.key}' detected in '${path.fileName}' after flattening categories."
            }
        }
    }

    override fun write(path: Path, root: CategoryItem): Boolean {
        return FileIO.write(path) { writer ->
            writer.write(ConfigIO.TOKEN_POUND)
            writer.write(path.fileName.toString())
            writer.write("\n\n")
            writeItems(writer, root.children)
        }
    }

    override fun read(path: Path, root: CategoryItem): Boolean {
        val properties = SharedPropertiesIO.read(path) { reason -> MConfigLogger.readFail(path, reason) } ?: return false

        for (option in root.getChildren()) {
            val raw = properties.getProperty(option.key) ?: continue
            if (!option.loadText(raw)) MConfigLogger.invalidValue(option)
        }
        return true
    }

    private fun writeItems(writer: Writer, items: List<ConfigItem>) {
        for (item in items) {
            when (item) {
                is CategoryItem -> {
                    writeCommentBlock(writer, item.comment)
                    writeItems(writer, item.children)
                }

                is CommentItem -> writeCommentBlock(writer, item.text)
                is ConfigOption<*> -> {
                    ConfigIO.eachCommentLine(item) { text, from, to -> writeComment(writer, text, from, to) }
                    writer.write(item.key)
                    writer.write("=")
                    writeEscaped(writer, item.toText())
                    writer.write("\n\n")
                }
            }
        }
    }

    private fun writeCommentBlock(writer: Writer, comment: String?) {
        if (ConfigIO.eachCommentLine(comment) { text, from, to -> writeComment(writer, text, from, to) }) writer.write("\n")
    }

    private fun writeComment(writer: Writer, text: String, from: Int, to: Int) {
        writer.write(ConfigIO.TOKEN_POUND)
        writer.write(text, from, to - from)
        writer.write("\n")
    }

    private fun writeEscaped(writer: Writer, text: String) {
        var last = 0
        for (i in text.indices) {
            val escape = when (text[i]) {
                '\\' -> "\\\\"
                '\n' -> "\\n"
                '\r' -> "\\r"
                '\t' -> "\\t"
                '\u000c' -> "\\f"
                ' ' -> if (i == 0) "\\ " else continue
                else -> continue
            }
            if (last < i) writer.write(text, last, i - last)
            writer.write(escape)
            last = i + 1
        }
        if (last < text.length) writer.write(text, last, text.length - last)
    }
}

internal object SharedPropertiesIO {

    fun read(path: Path, exception: (String?) -> Unit): Properties? {
        return FileIO.read(path, exception) { reader -> Properties().apply { load(reader) } }
    }
}