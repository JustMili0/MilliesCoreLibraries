package net.justmili.corelibs.v2.config.writers.text;

import net.justmili.corelibs.v1.config.entry.ConfigEntry;
import net.justmili.corelibs.v1.config.entry.ListConfigEntry;
import net.justmili.corelibs.v1.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.writers.FormatWriter;

import java.nio.file.Path;
import java.util.Map;

public class TextWriter implements FormatWriter {

    @Override
    public void write(Path path, CategoryItem root) {

    }

    @Override
    public void load(Path path, Map<String, ConfigEntry<?>> entries, Map<String, ListConfigEntry> listEntries) {

    }
}
