package net.justmili.corelibs.v2.config.writers;

import net.justmili.corelibs.v2.config.flags.CommentType;
import net.justmili.corelibs.v2.config.flags.OptionFlags;
import net.justmili.corelibs.v2.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.items.ConfigOption;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

public interface BaseWriter {

    void write(Path path, CategoryItem root);

    boolean load(Path path, CategoryItem root);

    default String hint(ConfigOption<?> option, CommentType comment) {
        var token = comment.token();
        var parts = new ArrayList<String>();

        if (option.restriction() != null) parts.add(option.restriction());
        parts.add("Default: " + option.defaultValue());
        if (!option.flags().isEmpty()) parts.add("Requires: " + listFlags(option));

        return token + String.join(" - ", parts);
    }

    default void writeComment(BufferedWriter writer, String comment, CommentType type, String indent) throws IOException {
        if (comment == null) return;

        for (var line : comment.split("\n")) writer.write(indent + type.token() + line + "\n");
    }

    private String listFlags(ConfigOption<?> option) {
        return option.flags().stream().map(OptionFlags::descName).collect(Collectors.joining(", "));
    }
}