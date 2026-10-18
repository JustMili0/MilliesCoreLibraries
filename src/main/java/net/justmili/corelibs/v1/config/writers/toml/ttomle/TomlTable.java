package net.justmili.corelibs.v1.config.writers.toml.ttomle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TomlTable extends TomlTableBase {

    public TomlTable(String name) {
        super(new TomlDottedElementName().addName(name, TomlStringType.DOUBLE));
    }
    public TomlTable(TomlDottedElementName name) {
        super(name);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < name.depth(); i++) {
            String item = name.getName(i);
            Pattern pattern = Pattern.compile("([A-Za-z0-9_-]+)");
            Matcher matcher = pattern.matcher(item);

            if (matcher.matches()) {
                builder.append(item);
            } else {
                builder.append("\"").append(TomlStringUtils.escapeTomlString(item)).append("\"");
            }
            if (i != (name.depth() - 1)) {
                builder.append(".");
            }
        }
        builder.append("]");
        return builder.toString();
    }

}
