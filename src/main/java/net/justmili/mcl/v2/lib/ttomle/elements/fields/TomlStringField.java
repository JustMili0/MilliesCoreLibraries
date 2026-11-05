package net.justmili.mcl.v2.lib.ttomle.elements.fields;

import net.justmili.mcl.v2.lib.ttomle.elements.TomlDottedElementName;
import net.justmili.mcl.v2.lib.ttomle.data.TomlStringType;
import net.justmili.mcl.v2.lib.ttomle.data.TomlString;

public class TomlStringField extends TomlField {

    public TomlStringField(TomlDottedElementName name, TomlString value) {
        super(name, value);
    }

    public TomlStringField(TomlDottedElementName name, String value, TomlStringType type) {
        super(name, new TomlString(value, type));
    }

    public void setValue(String value) {
        ((TomlString) this.value).value = value;
    }

    public void setStringType(TomlStringType type) {
        ((TomlString) this.value).type = type;
    }

    @Override
    public String toString() {
        if (((TomlString) value).type == TomlStringType.NO_QUOTE) {
            return "";
        }
        return name + " = " + value;
    }

}
