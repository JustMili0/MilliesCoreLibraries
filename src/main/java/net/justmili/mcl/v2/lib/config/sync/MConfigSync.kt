package net.justmili.mcl.v2.lib.config.sync

import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import net.justmili.mcl.v2.lib.config.MConfigLogger
import net.justmili.mcl.v2.lib.config.codecs.ConfigCodec
import net.justmili.mcl.v2.lib.config.items.CategoryItem

class MConfigSync(val id: String, private val root: CategoryItem, private val lock: Any) {
    private var sent: String? = null

    fun toJson(): String? = synchronized(lock) { encode()?.also { sent = it } }

    fun toChangedJson(): String? = synchronized(lock) {
        val json = encode()
        if (json == null || json == sent) null else json.also { sent = it }
    }

    fun apply(json: String) {
        val tree = try {
            JsonParser.parseString(json)
        } catch (e: JsonParseException) {
            MConfigLogger.syncFail(id, e.message)
            return
        }
        ConfigCodec.of(root, true).parse(JsonOps.INSTANCE, tree).error().ifPresent { error -> MConfigLogger.syncFail(id, error.message()) }
    }

    fun clear() = root.getChildren().forEach { option -> option.clearSynced() }

    private fun encode(): String? {
        val json = ConfigCodec.of(root).encodeStart(JsonOps.INSTANCE, root).resultOrPartial { error -> MConfigLogger.syncFail(id, error) }
        return json.map { it.toString() }.orElse(null)
    }
}