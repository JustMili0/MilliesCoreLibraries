package net.justmili.corelibs.v2.config.writers;

import net.justmili.corelibs.v1.config.entry.ConfigEntry;
import net.justmili.corelibs.v1.config.entry.ListConfigEntry;
import net.justmili.corelibs.v1.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.flags.CommentType;

import java.nio.file.Path;
import java.util.Map;

public interface FormatWriter {

    void write(Path path, CategoryItem root);

    void load(Path path, Map<String, ConfigEntry<?>> entries, Map<String, ListConfigEntry> listEntries);

    default String hint(ConfigEntry<?> entry, CommentType comment) {
        var token = comment.token();

        Object defaultValue = entry.defaultValue();
        if (entry.hasRange()) return token + "Allowed range: " + entry.min() + "-" + entry.max() + " - Default: " + defaultValue;
        if (defaultValue instanceof Boolean) return token + "Allowed values: true, false - Default: " + defaultValue;

        return token + "Default: " + defaultValue;
    }

    default String listHint(ListConfigEntry entry, CommentType comment) {
        var token = comment.token();

        return token + "Allowed types: " + entry.type().getSimpleName() + " - Default: " + entry.defaultValue();
    }
}