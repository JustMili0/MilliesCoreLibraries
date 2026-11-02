package net.justmili.mcl.v2.lib.config.writers.json;

import com.google.gson.JsonObject;
import net.justmili.mcl.v1.lib.util.common.FileIO;
import net.justmili.mcl.v2.lib.config.items.*;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;
import net.justmili.mcl.v2.lib.config.writers.ConfigIO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Json5IO implements ConfigIO {

    @Override
    public void write(Path path, CategoryItem root) {
        FileIO.write(path, writer -> {
            writer.write("{\n");
            writeItems(writer, root.getChildren(), SharedJsonIO.encode(root), INDENT);
            writer.write("}\n");
        });
    }

    private void writeItems(BufferedWriter writer, List<ConfigItem> items, JsonObject obj, String indent) throws IOException {
        int lastItemIndex = getLastItemIndex(items);
        for (int i = 0; i < items.size(); i++) {
            var item = items.get(i);
            boolean isLastItem = i == lastItemIndex;

            if (item instanceof CommentItem comment) {
                writeDescComment(writer, comment.getText(), TOKEN_SLASH, indent);

            } else if (item instanceof ConfigOption<?> option) {
                writeOptionComment(writer, option, TOKEN_SLASH, indent);
                writer.write(indent + SharedJsonIO.quote(option.getKey()) + ": " + SharedJsonIO.getValue(obj, option.getKey()) + (isLastItem? "\n" : ",\n"));
                if (!isLastItem) writer.write("\n");

            } else if (item instanceof CategoryItem cat) {
                writeDescComment(writer, cat.getComment(), TOKEN_SLASH, indent);
                writer.write(indent + SharedJsonIO.quote(cat.getName()) + ": {\n");
                writeItems(writer, cat.getChildren(), SharedJsonIO.getChild(obj, cat.getName()), indent + INDENT);
                writer.write(indent + "}" + (isLastItem? "\n" : ",\n"));

                if (!isLastItem) writer.write("\n");
            }
        }
    }

    // Returns the index of the last item that is not a comment
    private static int getLastItemIndex(List<ConfigItem> items) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (!(items.get(i) instanceof CommentItem)) return i;
        }
        return -1;
    }

    @Override
    public boolean load(Path path, CategoryItem root) {
        return SharedJsonIO.load(path, root);
    }
}