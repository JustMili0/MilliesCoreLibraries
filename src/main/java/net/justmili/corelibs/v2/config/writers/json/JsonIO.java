package net.justmili.corelibs.v2.config.writers.json;

import com.google.gson.JsonObject;
import net.justmili.corelibs.v1.util.common.FileIO;
import net.justmili.corelibs.v2.config.flags.CommentType;
import net.justmili.corelibs.v2.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.items.CommentItem;
import net.justmili.corelibs.v2.config.items.ConfigItem;
import net.justmili.corelibs.v2.config.items.ConfigOption;
import net.justmili.corelibs.v2.config.writers.BaseWriter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonIO implements BaseWriter {

    @Override
    public void write(Path path, CategoryItem root) {
        FileIO.write(path, writer -> writer.write("{\n" + String.join(",\n\n", render(root.children(), SharedJsonIO.encode(root), "  ", new int[]{0})) + "\n}\n"));
    }

    private List<String> render(List<ConfigItem> items, JsonObject json, String indent, int[] commentIndex) {
        var blocks = new ArrayList<String>();
        for (var item : items) {
            var lines = new ArrayList<String>();

            if (item instanceof CommentItem commentItem) {
                addComment(lines, commentItem.comment(), indent, commentIndex);

            } else if (item instanceof ConfigOption<?> option) {
                addComment(lines, option.comment(), indent, commentIndex);
                addComment(lines, hint(option, CommentType.NONE), indent, commentIndex);
                lines.add(indent + "\"" + option.key() + "\": " + SharedJsonIO.jsonValue(json, option.key()));

            } else if (item instanceof CategoryItem category) {
                addComment(lines, category.comment(), indent, commentIndex);
                var children = render(category.children(), SharedJsonIO.child(json, category.name()), indent + "  ", commentIndex);
                lines.add(indent + "\"" + category.name() + "\": {\n" + String.join(",\n\n", children) + "\n" + indent + "}");
            }

            if (!lines.isEmpty()) blocks.add(String.join(",\n", lines));
        }
        return blocks;
    }

    private void addComment(List<String> lines, String comment, String indent, int[] commentIndex) {
        if (comment == null) return;

        for (var line : comment.split("\n")) lines.add(indent + SharedJsonIO.quote(CommentType.KEY.token() + commentIndex[0]++) + ": " + SharedJsonIO.quote(line));
    }

    @Override
    public boolean load(Path path, CategoryItem root) {
        return SharedJsonIO.load(path, root);
    }
}