package net.justmili.mcl.v2.lib.config.serializers

import com.google.gson.*
import com.mojang.serialization.JsonOps
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.codecs.ConfigCodec
import net.justmili.mcl.v2.lib.config.items.*
import net.justmili.mcl.v2.lib.config.options.ConfigOption
import net.justmili.mcl.v2.lib.util.common.FileIO
import java.io.IOException
import java.io.Writer
import java.nio.file.Path

object JsonIO : ConfigIO {
    private const val BLOCK_SEPARATOR = ",\n\n"
    private const val LINE_SEPARATOR = ",\n"

    override fun write(path: Path, root: CategoryItem): Boolean {
        return FileIO.write(path) { writer ->
            writer.write("{\n")
            writeItems(writer, root.children, ConfigIO.INDENT, intArrayOf(0))
            writer.write("\n}\n")
        }
    }

    override fun read(path: Path, root: CategoryItem): Boolean {
        return SharedJsonIO.load(path, root)
    }

    private fun writeItems(writer: Writer, items: List<ConfigItem>, indent: String, commentIndex: IntArray) {
        var wroteBlock = false
        for (item in items) {
            var separator = if (wroteBlock) BLOCK_SEPARATOR else ""

            when (item) {
                is CommentItem -> ConfigIO.eachCommentLine(item.text) { text, from, to ->
                    writeComment(writer, separator, indent, commentIndex, text, from, to)
                    separator = LINE_SEPARATOR
                }

                is ConfigOption<*> -> {
                    ConfigIO.eachCommentLine(item) { text, from, to ->
                        writeComment(writer, separator, indent, commentIndex, text, from, to)
                        separator = LINE_SEPARATOR
                    }
                    writeKey(writer, separator, indent, item.key)
                    SharedJsonIO.writeValue(writer, item)
                    separator = LINE_SEPARATOR
                }

                is CategoryItem -> {
                    ConfigIO.eachCommentLine(item.comment) { text, from, to ->
                        writeComment(writer, separator, indent, commentIndex, text, from, to)
                        separator = LINE_SEPARATOR
                    }
                    writeKey(writer, separator, indent, item.name)
                    writer.write("{\n")
                    writeItems(writer, item.children, indent + ConfigIO.INDENT, commentIndex)
                    writer.write("\n")
                    writer.write(indent)
                    writer.write("}")
                    separator = LINE_SEPARATOR
                }
            }

            if (separator == LINE_SEPARATOR) wroteBlock = true
        }
    }

    private fun writeComment(writer: Writer, separator: String, indent: String, commentIndex: IntArray, text: String, from: Int, to: Int) {
        writer.write(separator)
        writer.write(indent)
        writer.write("\"" + ConfigIO.TOKEN_KEY)
        writer.write(commentIndex[0]++.toString())
        writer.write("\": ")
        SharedJsonIO.quote(writer, text, from, to)
    }

    private fun writeKey(writer: Writer, separator: String, indent: String, key: String) {
        writer.write(separator)
        writer.write(indent)
        SharedJsonIO.quote(writer, key, 0, key.length)
        writer.write(": ")
    }
}

object Json5IO : ConfigIO {

    override fun write(path: Path, root: CategoryItem): Boolean {
        return FileIO.write(path) { writer ->
            writer.write("{\n")
            writeItems(writer, root.children, ConfigIO.INDENT)
            writer.write("}\n")
        }
    }

    override fun read(path: Path, root: CategoryItem): Boolean {
        return SharedJsonIO.load(path, root)
    }

    private fun writeItems(writer: Writer, items: List<ConfigItem>, indent: String) {
        val lastItemIndex = items.indexOfLast { it !is CommentItem }

        for (i in items.indices) {
            val item = items[i]
            val isLastItem = i == lastItemIndex

            when (item) {
                is CommentItem -> {
                    val wrote = ConfigIO.eachCommentLine(item.text) { text, from, to -> writeComment(writer, indent, text, from, to) }
                    if (wrote && i != items.lastIndex) writer.write("\n")
                }

                is ConfigOption<*> -> {
                    ConfigIO.eachCommentLine(item) { text, from, to -> writeComment(writer, indent, text, from, to) }
                    writeKey(writer, indent, item.key)
                    SharedJsonIO.writeValue(writer, item)
                    writer.write(if (isLastItem) "\n" else ",\n\n")
                }

                is CategoryItem -> {
                    ConfigIO.eachCommentLine(item.comment) { text, from, to -> writeComment(writer, indent, text, from, to) }
                    writeKey(writer, indent, item.name)
                    writer.write("{\n")
                    writeItems(writer, item.children, indent + ConfigIO.INDENT)
                    writer.write(indent)
                    writer.write(if (isLastItem) "}\n" else "},\n\n")
                }
            }
        }
    }

    private fun writeComment(writer: Writer, indent: String, text: String, from: Int, to: Int) {
        writer.write(indent)
        writer.write(ConfigIO.TOKEN_SLASH)
        writer.write(text, from, to - from)
        writer.write("\n")
    }

    private fun writeKey(writer: Writer, indent: String, key: String) {
        writer.write(indent)
        SharedJsonIO.quote(writer, key, 0, key.length)
        writer.write(": ")
    }
}

private object SharedJsonIO {
    private const val HEX = "0123456789abcdef"

    fun load(path: Path, root: CategoryItem): Boolean {
        return try {
            FileIO.bufferedReader(path).use { reader -> ConfigCodec.of(root).parse(JsonOps.INSTANCE, JsonParser.parseReader(reader)).resultOrPartial { error -> MConfigLogger.readFail(path, error) }.isPresent }
        } catch (e: IOException) {
            MConfigLogger.readFail(path, e.message)
            false
        } catch (e: JsonParseException) {
            MConfigLogger.readFail(path, e.message)
            false
        }
    }

    @Throws(IOException::class)
    fun writeValue(writer: Writer, option: ConfigOption<*>) {
        val element = option.encode(JsonOps.INSTANCE).orElse(null)
        if (element !is JsonPrimitive) {
            writer.write(element?.toString() ?: "null")
        } else if (element.isString) {
            val text = element.asString
            quote(writer, text, 0, text.length)
        } else {
            writer.write(element.asString)
        }
    }

    @Throws(IOException::class)
    fun quote(writer: Writer, text: String, from: Int, to: Int) {
        writer.write('"'.code)
        var last = from
        for (i in from until to) {
            val c = text[i]
            val escape = when {
                c == '"' -> "\\\""
                c == '\\' -> "\\\\"
                c == '\n' -> "\\n"
                c == '\r' -> "\\r"
                c == '\t' -> "\\t"
                c == '\b' -> "\\b"
                c == '\u000c' -> "\\f"
                c < ' ' -> "\\u00" + HEX[c.code shr 4] + HEX[c.code and 15]
                c == '\u2028' -> "\\u2028"
                c == '\u2029' -> "\\u2029"
                else -> continue
            }
            if (last < i) writer.write(text, last, i - last)
            writer.write(escape)
            last = i + 1
        }
        if (last < to) writer.write(text, last, to - last)
        writer.write('"'.code)
    }
}