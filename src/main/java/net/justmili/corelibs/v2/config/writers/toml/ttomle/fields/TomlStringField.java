package net.justmili.corelibs.v2.config.writers.toml.ttomle.fields;

import net.justmili.corelibs.v2.config.writers.toml.ttomle.TomlDottedElementName;
import net.justmili.corelibs.v2.config.writers.toml.ttomle.TomlStringType;
import net.justmili.corelibs.v2.config.writers.toml.ttomle.data.TomlString;

public class TomlStringField extends TomlField {

    public TomlStringField(String name, String value) {
        super(TomlDottedElementName.fromString(name), new TomlString(value, TomlStringType.DOUBLE));
    }

    public TomlStringField(TomlDottedElementName name, String value, TomlStringType type) {
        super(name, new TomlString(value, type));
    }

    public void setValue(String value) {
        ((TomlString) this.value).value = value;
    }

    public void setStringType(TomlStringType type) {
        ((TomlString) this.value).type = type;
    }

    @Override
    public String toString() {
        if (((TomlString) value).type == TomlStringType.NO_QUOTE) {
            return "";
        }
        return name + " = " + value;
    }

}
