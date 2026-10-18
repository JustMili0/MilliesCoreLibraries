package net.justmili.corelibs.v1.config.writers.toml.ttomle.fields;

import net.justmili.corelibs.v1.config.writers.toml.ttomle.TomlDottedElementName;
import net.justmili.corelibs.v1.config.writers.toml.ttomle.data.TomlBoolean;

public class TomlBooleanField extends TomlField {

    public TomlBooleanField(String name, boolean value) {
        super(TomlDottedElementName.fromString(name), new TomlBoolean(value));
        this.value = new TomlBoolean(value);
    }

    public TomlBooleanField(TomlDottedElementName name, boolean value) {
        super(name, new TomlBoolean(value));
        this.value = new TomlBoolean(value);
    }

    public void setValue(boolean value) {
        this.value = new TomlBoolean(value);
    }

    public void toggle() {
        ((TomlBoolean) this.value).value = !((TomlBoolean) this.value).value;
    }

    @Override
    public String toString() {
        return name + " = " + value.toString();
    }
}
