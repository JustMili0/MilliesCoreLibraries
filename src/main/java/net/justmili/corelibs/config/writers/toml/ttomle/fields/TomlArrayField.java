package net.justmili.corelibs.config.writers.toml.ttomle.fields;

import net.justmili.corelibs.config.writers.toml.ttomle.TomlDottedElementName;
import net.justmili.corelibs.config.writers.toml.ttomle.data.TomlArray;
import net.justmili.corelibs.config.writers.toml.ttomle.data.TomlVariable;

import java.util.List;

public class TomlArrayField extends TomlField {

    public TomlArrayField(TomlDottedElementName name, List<TomlVariable> array) {
        super(name, new TomlArray(array));
    }

    public TomlArrayField(String name, List<TomlVariable> array) {
        super(TomlDottedElementName.fromString(name), new TomlArray(array));
    }
}
