package net.justmili.corelibs.v2.config.writers.toml.ttomle.data;

public class TomlInteger extends TomlVariable {

    public TomlIntRepresentation representation = TomlIntRepresentation.DECIMAL; // currently unused but is useful when adding binary, hexadecimal or octal support
    public long value;

    public TomlInteger(long value) {
        this.value = value;
    }
}
