package net.justmili.mcl.v2.lib.config.writers.json;

import com.google.gson.JsonObject;
import net.justmili.mcl.v1.lib.util.common.FileIO;
import net.justmili.mcl.v2.lib.config.items.*;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;
import net.justmili.mcl.v2.lib.config.writers.FileWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Json5IO implements FileWriter {

    @Override
    public void write(Path path, CategoryItem root) {
        FileIO.write(path, writer -> {
            writer.write("{\n");
            writeItems(writer, root.getChildren(), SharedJsonIO.encode(root), INDENT);
            writer.write("}\n");
        });
    }

    private void writeItems(BufferedWriter writer, List<ConfigItem> items, JsonObject obj, String indent) throws IOException {
        int lastReal = SharedJsonIO.getLastRealIndex(items);
        for (int i = 0; i < items.size(); i++) {
            var item = items.get(i);
            boolean last = i == lastReal;

            if (item instanceof CommentItem comment) {
                writeDescComment(writer, comment.getText(), TOKEN_SLASH, indent);

            } else if (item instanceof ConfigOption<?> option) {
                writeOptionComment(writer, option, TOKEN_SLASH, indent);
                writer.write(indent + SharedJsonIO.quote(option.getKey()) + ": " + SharedJsonIO.getValue(obj, option.getKey()) + (last? "\n" : ",\n"));
                if (!last) writer.write("\n");

            } else if (item instanceof CategoryItem cat) {
                writeDescComment(writer, cat.getComment(), TOKEN_SLASH, indent);
                writer.write(indent + SharedJsonIO.quote(cat.getName()) + ": {\n");
                writeItems(writer, cat.getChildren(), SharedJsonIO.getChild(obj, cat.getName()), indent + INDENT);
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