package net.justmili.mcl.v2.lib.config.writers.toml

import net.justmili.mcl.v2.lib.config.items.CategoryItem
import net.justmili.mcl.v2.lib.config.writers.FileWriter
import java.nio.file.Path

class TomlIO : FileWriter {

    override fun write(path: Path, root: CategoryItem) {
        TODO("Not yet implemented")
    }

    override fun load(path: Path, root: CategoryItem): Boolean {
        TODO("Not yet implemented")
    }
}