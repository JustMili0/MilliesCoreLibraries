package net.justmili.corelibs.v2.config.writers.json;

import com.google.gson.JsonObject;
import net.justmili.corelibs.v1.util.common.FileIO;
import net.justmili.corelibs.v2.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.items.CommentItem;
import net.justmili.corelibs.v2.config.items.ConfigItem;
import net.justmili.corelibs.v2.config.items.ConfigOption;
import net.justmili.corelibs.v2.config.writers.FileWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Json5IO implements FileWriter {

    @Override
    public void write(Path path, CategoryItem root) {
        FileIO.write(path, writer -> {
            writer.write("{\n");
            writeItems(writer, root.children(), SharedJsonIO.encode(root), INDENT);
            writer.write("}\n");
        });
    }

    private void writeItems(BufferedWriter writer, List<ConfigItem> items, JsonObject obj, String indent) throws IOException {
        int lastReal = SharedJsonIO.lastRealIndex(items);
        for (int i = 0; i < items.size(); i++) {
            var item = items.get(i);
            boolean last = i == lastReal;

            if (item instanceof CommentItem comment) {
                writeComment(writer, comment.comment, TOKEN_SLASH, indent);

            } else if (item instanceof ConfigOption<?> option) {
                writeComment(writer, option.comment(), TOKEN_SLASH, indent);
                writer.write(indent + hint(option, TOKEN_SLASH) + "\n");
                writer.write(indent + SharedJsonIO.quote(option.key()) + ": " + SharedJsonIO.getValue(obj, option.key()) + (last? "\n" : ",\n"));
                if (!last) writer.write("\n");

            } else if (item instanceof CategoryItem cat) {
                writeComment(writer, cat.comment(), TOKEN_SLASH, indent);
                writer.write(indent + SharedJsonIO.quote(cat.name()) + ": {\n");
                writeItems(writer, cat.children(), SharedJsonIO.getChild(obj, cat.name()), indent + INDENT);
                writer.write(indent + "}" + (last? "\n" : ",\n"));

                if (!last) writer.write("\n");
            }
        }
    }

    @Override
    public boolean load(Path path, CategoryItem root) {
        return SharedJsonIO.load(path, root);
    }
}