package net.justmili.mcl.v2.lib.config.writers.toml.ttomle.fields;

import net.justmili.mcl.v2.lib.config.writers.toml.ttomle.TomlDottedElementName;
import net.justmili.mcl.v2.lib.config.writers.toml.ttomle.TomlElement;
import net.justmili.mcl.v2.lib.config.writers.toml.ttomle.data.TomlVariable;

public class TomlField extends TomlElement {

    public TomlVariable value;

    public TomlField(TomlDottedElementName name, TomlVariable value) {
        super();
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " = " + value.toString();
    }

}
