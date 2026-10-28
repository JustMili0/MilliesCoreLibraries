package net.justmili.corelibs.v2.config.writers;

import net.justmili.corelibs.v2.config.OptionFlags;
import net.justmili.corelibs.v2.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.items.ConfigOption;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

public interface FileWriter {
    String TOKEN_NONE = "";
    String TOKEN_SLASH = "// ";
    String TOKEN_POUND = "# ";
    String TOKEN_KEY = "_c";
    String INDENT = "  ";

    void write(Path path, CategoryItem root);

    boolean load(Path path, CategoryItem root);

    default String hint(ConfigOption<?> option, String token) {
        var parts = new ArrayList<String>();

        if (option.restriction() != null) parts.add(option.restriction());
        parts.add("Default: " + String.valueOf(option.defaultValue()).replace("\r", "\\r").replace("\n", "\\n"));
        if (!option.flags().isEmpty()) parts.add("Requires: " + listFlags(option));

        return token + String.join(" - ", parts);
    }

    default void writeComment(BufferedWriter writer, String comment, String token, String indent) throws IOException {
        if (comment == null) return;
        for (var line : comment.split("\n")) writer.write(indent + token + line + "\n");
    }

    private String listFlags(ConfigOption<?> option) {
        return option.flags().stream().map(OptionFlags::descName).collect(Collectors.joining(", "));
    }
}