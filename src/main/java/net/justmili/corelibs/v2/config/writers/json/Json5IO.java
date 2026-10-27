package net.justmili.corelibs.v2.config.writers.json;

import com.google.gson.JsonObject;
import net.justmili.corelibs.v1.util.common.FileIO;
import net.justmili.corelibs.v2.config.flags.CommentType;
import net.justmili.corelibs.v2.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.items.CommentItem;
import net.justmili.corelibs.v2.config.items.ConfigItem;
import net.justmili.corelibs.v2.config.items.ConfigOption;
import net.justmili.corelibs.v2.config.writers.BaseWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Json5IO implements BaseWriter {

    @Override
    public void write(Path path, CategoryItem root) {
        FileIO.write(path, writer -> {
            writer.write("{\n");
            writeItems(writer, root.children(), SharedJsonIO.encode(root), "  ");
            writer.write("}\n");
        });
    }

    private void writeItems(BufferedWriter writer, List<ConfigItem> items, JsonObject json, String indent) throws IOException {
        int lastReal = SharedJsonIO.lastRealIndex(items, true);
        for (int i = 0; i < items.size(); i++) {
            var item = items.get(i);
            boolean last = i == lastReal;

            if (item instanceof CommentItem commentItem) {
                writeComment(writer, commentItem.comment(), CommentType.SLASH, indent);

            } else if (item instanceof ConfigOption<?> option) {
                writeComment(writer, option.comment(), CommentType.SLASH, indent);
                writer.write(indent + hint(option, CommentType.SLASH) + "\n");
                writer.write(indent + "\"" + option.key() + "\": " + SharedJsonIO.jsonValue(json, option.key()) + (last? "\n" : ",\n"));
                if (!last) writer.write("\n");

            } else if (item instanceof CategoryItem category) {
                writeComment(writer, category.comment(), CommentType.SLASH, indent);
                writer.write(indent + "\"" + category.name() + "\": {\n");
                writeItems(writer, category.children(), SharedJsonIO.child(json, category.name()), indent + "  ");
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