package net.justmili.mcl.v2.lib.ttomle.elements.fields;

import net.justmili.mcl.v2.lib.ttomle.data.TomlVariable;
import net.justmili.mcl.v2.lib.ttomle.elements.TomlDottedElementName;
import net.justmili.mcl.v2.lib.ttomle.elements.TomlElement;

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
