package net.justmili.corelibs.v2.config.writers.toml.ttomle.data;

import net.justmili.corelibs.v2.config.writers.toml.ttomle.TomlBuildingException;
import net.justmili.corelibs.v2.config.writers.toml.ttomle.TomlUtils;

public class TomlDecimal extends TomlVariable {

    public double value;
    public TomlInteger exponent;
    public boolean hasExponent;

    public TomlDecimal(double value) {
        this.value = value;
        this.exponent = new TomlInteger(0);
        this.hasExponent = false;
    }

    public TomlDecimal(double value, TomlInteger exponent) {
        this.value = value;
        this.exponent = exponent;
        this.hasExponent = true;
    }

    @Override
    public String toString() {
        if (hasExponent) {
            if (value > (10 ^ 7)) {
                try {
                    throw new TomlBuildingException("Decimal with value: " + value + " and exponent " + exponent + " is too large have a separate exponent");
                } catch (TomlBuildingException e) {
                    throw new RuntimeException(e);
                }
            }
            if (value < -(10 ^ 3)) {
                try {
                    throw new TomlBuildingException("Decimal with value: " + value + " and exponent " + exponent + " is too small have a separate exponent");
                } catch (TomlBuildingException e) {
                    throw new RuntimeException(e);
                }
            }
            return TomlUtils.toTomlFloatString(value) + "E" + exponent;
        }
        return TomlUtils.toTomlFloatString(value);
    }

}
