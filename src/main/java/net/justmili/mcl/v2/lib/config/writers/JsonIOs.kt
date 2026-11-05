package net.justmili.mcl.v2.lib.config.writers

import com.google.gson.*
import com.mojang.serialization.JsonOps
import net.justmili.mcl.CoreLibs
import net.justmili.mcl.v1.lib.util.common.FileIO
import net.justmili.mcl.v2.lib.config.MConfigLoader
import net.justmili.mcl.v2.lib.config.codecs.ConfigCodec
import net.justmili.mcl.v2.lib.config.items.*
import net.justmili.mcl.v2.lib.config.options.ConfigOption
import java.io.BufferedWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class JsonIO : ConfigIO {

    override fun write(path: Path, root: CategoryItem) {
        FileIO.write(path) {writer -> writer.write("{\n" + render(root.getChildren(), SharedJsonIO.encode(root), ConfigIO.INDENT, intArrayOf(0)).joinToString(",\n\n") + "\n}\n")}
    }

    private fun render(items: List<ConfigItem>, obj: JsonObject, indent: String, commentIndex: IntArray): List<String> {
        val blocks = ArrayList<String>()
        for (item in items) {
            val lines = ArrayList<String>()

            when (item) {
                is CommentItem -> addComment(lines, addPeriod(item.text), indent, commentIndex)

                is ConfigOption<*> -> {
                    addComment(lines, addPeriod(item.comment), indent, commentIndex)
                    for (line in getDetails(item)) {
                        addComment(lines, line, indent, commentIndex)
                    }
                    lines.add(indent + SharedJsonIO.quote(item.key) + ": " + SharedJsonIO.getValue(obj, item.key))
                }

                is CategoryItem -> {
                    addComment(lines, addPeriod(item.getComment()), indent, commentIndex)
                    val children = render(item.getChildren(), SharedJsonIO.getChild(obj, item.getName()), indent + ConfigIO.INDENT, commentIndex)
                    lines.add(indent + SharedJsonIO.quote(item.getName()) + ": {\n" + children.joinToString(",\n\n") + "\n" + indent + "}")
                }
            }

            if (lines.isNotEmpty()) blocks.add(lines.joinToString(",\n"))
        }
        return blocks
    }

    private fun addComment(lines: MutableList<String>, comment: String?, indent: String, commentIndex: IntArray) {
        if (comment == null) return
        for (line in comment.split("\n")) {
            lines.add(indent + SharedJsonIO.quote(ConfigIO.TOKEN_KEY + commentIndex[0]++) + ": " + SharedJsonIO.quote(line))
        }
    }

    override fun load(path: Path, root: CategoryItem): Boolean {
        return SharedJsonIO.load(path, root)
    }
}

class Json5IO : ConfigIO {

    override fun write(path: Path, root: CategoryItem) {
        FileIO.write(path) {writer ->
            writer.write("{\n")
            writeItems(writer, root.getChildren(), SharedJsonIO.encode(root), ConfigIO.INDENT)
            writer.write("}\n")
        }
    }

    private fun writeItems(writer: BufferedWriter, items: List<ConfigItem>, obj: JsonObject, indent: String) {
        // Returns the index of the last item that is not a comment
        val lastItemIndex = items.indexOfLast {it !is CommentItem}

        for ((i, item) in items.withIndex()) {
            val isLastItem = i == lastItemIndex

            when (item) {
                is CommentItem -> writeDescComment(writer, item.text, ConfigIO.TOKEN_SLASH, indent)

                is ConfigOption<*> -> {
                    writeOptionComment(writer, item, ConfigIO.TOKEN_SLASH, indent)
                    writer.write(indent + SharedJsonIO.quote(item.key) + ": " + SharedJsonIO.getValue(obj, item.key) + (if (isLastItem) "\n" else ",\n"))
                    if (!isLastItem) writer.write("\n")
                }

                is CategoryItem -> {
                    writeDescComment(writer, item.getComment(), ConfigIO.TOKEN_SLASH, indent)
                    writer.write(indent + SharedJsonIO.quote(item.getName()) + ": {\n")
                    writeItems(writer, item.getChildren(), SharedJsonIO.getChild(obj, item.getName()), indent + ConfigIO.INDENT)
                    writer.write(indent + "}" + (if (isLastItem) "\n" else ",\n"))

                    if (!isLastItem) writer.write("\n")
                }
            }
        }
    }

    override fun load(path: Path, root: CategoryItem): Boolean {
        return SharedJsonIO.load(path, root)
    }
}

private object SharedJsonIO {

    fun encode(root: CategoryItem): JsonObject {
        val encoded = ConfigCodec.of(root).encodeStart(JsonOps.INSTANCE, root).resultOrPartial {error -> CoreLibs.LOGGER.error("Failed to encode config: {}", error)}.orElse(null)
        return encoded as? JsonObject ?: JsonObject()
    }

    fun load(path: Path, root: CategoryItem): Boolean {
        return try {
            Files.newBufferedReader(path).use {reader ->
                ConfigCodec.of(root).parse(JsonOps.INSTANCE, JsonParser.parseReader(reader)).resultOrPartial {error -> MConfigLoader.loadError(error)}.isPresent
            }
        } catch (e: IOException) {
            MConfigLoader.loadError(e.message)
            false
        } catch (e: JsonParseException) {
            MConfigLoader.loadError(e.message)
            false
        }
    }

    fun getValue(obj: JsonObject, key: String): String {
        return if (obj.has(key)) obj.get(key).toString() else "null"
    }

    fun getChild(obj: JsonObject, name: String): JsonObject {
        return obj.get(name) as? JsonObject ?: JsonObject()
    }

    fun quote(text: String): String {
        return JsonPrimitive(text).toString()
    }
}