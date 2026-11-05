package net.justmili.mcl.v2.lib.ttomle.elements.fields;

import net.justmili.mcl.v2.lib.ttomle.elements.TomlDottedElementName;
import net.justmili.mcl.v2.lib.ttomle.data.TomlInteger;

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

    /*@Override
    public String toString() {
        return name + " = " + value.toString();
    }
     */
}
