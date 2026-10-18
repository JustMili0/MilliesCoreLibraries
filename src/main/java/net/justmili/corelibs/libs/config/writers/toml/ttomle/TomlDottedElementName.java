package net.justmili.corelibs.libs.config.writers.toml.ttomle;
import java.util.ArrayList;
import java.util.List;

public class TomlDottedElementName {

    public List<String> name = new ArrayList<>();
    public List<TomlStringType> stringTypes = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < name.size(); i++) {
            String item = name.get(i);
            TomlStringType type = stringTypes.get(i);

            switch (type) {
                case DOUBLE -> {
                    builder.append('"').append(TomlStringUtils.escapeTomlString(item)).append('"');
                }
                case NO_QUOTE -> {
                    builder.append(item);
                }
            }

            if (i != (name.size() - 1)) {
                builder.append(".");
            }
        }
        return builder.toString();
    }

    public TomlDottedElementName addName(String name, TomlStringType type) {
        this.name.add(name);
        this.stringTypes.add(type);
        return this;
    }

    public static TomlDottedElementName fromString(String string) {
        TomlDottedElementName name = new TomlDottedElementName();
        TomlStringType type = TomlStringUtils.isNoQuoteStringValid(string)
                ? TomlStringType.NO_QUOTE
                : TomlStringType.DOUBLE;
        name.addName(string, type);
        return name;
    }

    public String getName(int index) {
        return name.get(index);
    }

    public TomlStringType getTomlType(int index) {
        return stringTypes.get(index);
    }

    public int depth() {
        return name.size();
    }

}
