package net.justmili.mcl.v2.lib.config.serializers

import net.justmili.mcl.v2.lib.config.items.CategoryItem
import java.nio.file.Path

class TomlIO : ConfigIO {

    // Note: TTOMLE was moved to v2.lib.ttomle so it doesn't have to be 100% specific to just the config lib and can be used elsewhere too

    override fun write(path: Path, root: CategoryItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun load(path: Path, root: CategoryItem): Boolean {
        TODO("Not yet implemented")
    }
}