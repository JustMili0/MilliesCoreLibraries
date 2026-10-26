package net.justmili.corelibs.v2.config.writers.toml.ttomle.fields;

import net.justmili.corelibs.v2.config.writers.toml.ttomle.TomlDottedElementName;
import net.justmili.corelibs.v2.config.writers.toml.ttomle.data.TomlDecimal;
import net.justmili.corelibs.v2.config.writers.toml.ttomle.data.TomlInteger;

public class TomlDecimalField extends TomlField {

    public TomlDecimalField(String name, double value) {
        super(TomlDottedElementName.fromString(name), new TomlDecimal(value));
    }

    public TomlDecimalField(TomlDottedElementName name, double value) {
        super(name, new TomlDecimal(value));
    }

    public TomlDecimalField(String name, double value, TomlInteger exponent) {
        super(TomlDottedElementName.fromString(name), new TomlDecimal(value, exponent));
    }

    public TomlDecimalField(TomlDottedElementName name, double value, TomlInteger exponent) {
        super(name, new TomlDecimal(value, exponent));
    }

    public TomlDecimalField(String name, double value, long exponent) {
        super(TomlDottedElementName.fromString(name), new TomlDecimal(value, new TomlInteger(exponent)));
    }

    public TomlDecimalField(TomlDottedElementName name, double value, long exponent) {
        super(name, new TomlDecimal(value, new TomlInteger(exponent)));
    }

    public void setExponent(long exponent) {
        ((TomlDecimal) this.value).exponent = new TomlInteger(exponent);
        ((TomlDecimal) this.value).hasExponent = true;
    }

    public void setExponent(TomlInteger exponent) {
        ((TomlDecimal) this.value).exponent = exponent;
        ((TomlDecimal) this.value).hasExponent = true;
    }


    public void setValue(double value) {
        this.value = new TomlDecimal(value);
    }

    @Override
    public String toString() {
        return name + " = " + value;
    }
}
