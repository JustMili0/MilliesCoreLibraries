package net.justmili.mcl.v2.lib.config.writers;

import net.justmili.mcl.v2.lib.config.items.CategoryItem;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface FileWriter {
    String TOKEN_SLASH = "// ";
    String TOKEN_POUND = "# ";
    String TOKEN_KEY = "_c";
    String NO_INDENT = "";
    String INDENT = "  ";

    void write(Path path, CategoryItem root);

    boolean load(Path path, CategoryItem root);

    default List<String> getDetails(ConfigOption<?> option) {
        var lines = new ArrayList<String>();
        if (!option.flags().isEmpty()) lines.add("Requires: " + listFlags(option));

        var parts = new ArrayList<String>();
        if (option.restriction() != null) parts.add(option.restriction());
        parts.add("Default: " + String.valueOf(option.getDefault()).replace("\r", "\\r").replace("\n", "\\n"));
        lines.add(String.join(" - ", parts));

        return lines;
    }

    default String addPeriod(String comment) {
        if (comment == null || comment.isBlank()) return null;

        var stripped = comment.stripTrailing();
        for (var pun : List.of(".", "!", "?", ":", ";", "*", "-", "~")) {
            if (stripped.endsWith(pun)) return stripped;
        }
        return stripped + ".";
    }

    default void writeDescComment(BufferedWriter writer, String comment, String token, String indent) throws IOException {
        var text = addPeriod(comment);
        if (text == null) return;
        for (var line : text.split("\n")) {
            writer.write(indent + token + line + "\n");
        }
    }

    default void writeOptionComment(BufferedWriter writer, ConfigOption<?> option, String token, String indent) throws IOException {
        writeDescComment(writer, option.comment(), token, indent);
        for (var line : getDetails(option)) {
            writer.write(indent + token + line + "\n");
        }
    }

    private String listFlags(ConfigOption<?> option) {
        var flags = new ArrayList<String>();
        for (var flag : option.flags()) {
            flags.add(flag.descName());
        }
        return String.join(", ", flags);
    }
}