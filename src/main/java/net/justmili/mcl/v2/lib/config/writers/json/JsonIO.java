package net.justmili.mcl.v2.lib.config.writers.json;

import com.google.gson.JsonObject;
import net.justmili.mcl.v1.lib.util.common.FileIO;
import net.justmili.mcl.v2.lib.config.option.ConfigOption;
import net.justmili.mcl.v2.lib.config.items.*;
import net.justmili.mcl.v2.lib.config.writers.FileWriter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonIO implements FileWriter {

    @Override
    public void write(Path path, CategoryItem root) {
        FileIO.write(path, writer -> writer.write("{\n" + String.join(",\n\n", render(root.children(), SharedJsonIO.encode(root), INDENT, new int[]{0})) + "\n}\n"));
    }

    private List<String> render(List<ConfigItem> items, JsonObject obj, String indent, int[] commentIndex) {
        var blocks = new ArrayList<String>();
        for (var item : items) {
            var lines = new ArrayList<String>();

            if (item instanceof CommentItem comment) {
                addComment(lines, addPeriod(comment.text), indent, commentIndex);

            } else if (item instanceof ConfigOption<?> option) {
                addComment(lines, addPeriod(option.comment()), indent, commentIndex);
                for (var line : getDetails(option)) {
                    addComment(lines, line, indent, commentIndex);
                }
                lines.add(indent + SharedJsonIO.quote(option.key()) + ": " + SharedJsonIO.getValue(obj, option.key()));

            } else if (item instanceof CategoryItem cat) {
                addComment(lines, addPeriod(cat.comment()), indent, commentIndex);
                var children = render(cat.children(), SharedJsonIO.getChild(obj, cat.name()), indent + INDENT, commentIndex);
                lines.add(indent + SharedJsonIO.quote(cat.name()) + ": {\n" + String.join(",\n\n", children) + "\n" + indent + "}");
            }

            if (!lines.isEmpty()) blocks.add(String.join(",\n", lines));
        }
        return blocks;
    }

    private void addComment(List<String> lines, String comment, String indent, int[] commentIndex) {
        if (comment == null) return;
        for (var line : comment.split("\n")) {
            lines.add(indent + SharedJsonIO.quote(TOKEN_KEY + commentIndex[0]++) + ": " + SharedJsonIO.quote(line));
        }
    }

    @Override
    public boolean load(Path path, CategoryItem root) {
        return SharedJsonIO.load(path, root);
    }
}