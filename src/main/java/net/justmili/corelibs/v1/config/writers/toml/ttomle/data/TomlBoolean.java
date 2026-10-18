package net.justmili.corelibs.v1.config.writers.toml.ttomle.data;

public class TomlBoolean extends TomlVariable {

    public boolean value;

    public TomlBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
