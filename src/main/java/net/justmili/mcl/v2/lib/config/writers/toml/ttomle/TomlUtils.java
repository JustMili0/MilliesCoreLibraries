package net.justmili.mcl.v2.lib.config.writers.toml.ttomle;

public class TomlUtils {

    public static String toTomlFloatString(double value) {
        if (Double.isNaN(value)) {
            return "nan";
        }
        if (value == Double.NEGATIVE_INFINITY) {
            return "-inf";
        }
        if (value == Double.POSITIVE_INFINITY) {
            return "+inf";
        }

        return Double.toString(value);
    }

}
