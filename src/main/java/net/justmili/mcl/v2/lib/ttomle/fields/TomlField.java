package net.justmili.mcl.v2.lib.ttomle.fields;

import net.justmili.mcl.v2.lib.ttomle.TomlDottedElementName;
import net.justmili.mcl.v2.lib.ttomle.TomlElement;
import net.justmili.mcl.v2.lib.ttomle.data.TomlVariable;

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
