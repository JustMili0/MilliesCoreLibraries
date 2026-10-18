package net.justmili.corelibs.libs.config.writers.toml.ttomle.fields;

import net.justmili.corelibs.libs.config.writers.toml.ttomle.TomlDottedElementName;
import net.justmili.corelibs.libs.config.writers.toml.ttomle.data.TomlInteger;

public class TomlIntegerField extends TomlField {

    public TomlIntegerField(String name, long value) {
        super(TomlDottedElementName.fromString(name), new TomlInteger(value));
    }

    public TomlIntegerField(TomlDottedElementName name, long value) {
        super(name, new TomlInteger(value));
    }

    public void setValue(long value) {
        this.value = new TomlInteger(value);
    }

    @Override
    public String toString() {
        return name + " = " + value;
    }
}
