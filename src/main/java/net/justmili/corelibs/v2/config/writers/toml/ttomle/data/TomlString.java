package net.justmili.corelibs.v2.config.writers.toml.ttomle.data;

import net.justmili.corelibs.v2.config.writers.toml.ttomle.TomlStringType;
import net.justmili.corelibs.v2.config.writers.toml.ttomle.TomlStringUtils;

public class TomlString extends TomlVariable {

    public String value;
    public TomlStringType type;

    public TomlString(String value) {
        this.value = value;
        this.type = TomlStringType.DOUBLE;
    }

    public TomlString(String value, TomlStringType type) {
        this.value = value;
        this.type = type;
    }

    // maybe validate LITERAL strings?    public String toString() {
    @Override
    public String toString() {
        switch (type) {
            case DOUBLE -> {
                return "\"" + TomlStringUtils.escapeTomlString(value) + "\"";
            }
            case LITERAL -> {
                return "'" + value + "'";
            }
            case TRIPLE_DOUBLE -> {
                return "\"\"\"" + value + "\"\"\"";
            }
            case NO_QUOTE -> {
                return "";
            }
        }
        return "";
    }

}
