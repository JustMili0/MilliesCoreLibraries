package net.justmili.mcl.v2.lib.ttomle.elements.fields;

import net.justmili.mcl.v2.lib.ttomle.elements.TomlDottedElementName;
import net.justmili.mcl.v2.lib.ttomle.data.TomlArray;
import net.justmili.mcl.v2.lib.ttomle.data.TomlVariable;

import java.util.List;

public class TomlArrayField extends TomlField {

    public TomlArrayField(TomlDottedElementName name, List<TomlVariable> array) {
        super(name, new TomlArray(array));
    }

    public TomlArrayField(String name, List<TomlVariable> array) {
        super(TomlDottedElementName.fromString(name), new TomlArray(array));
    }
}
