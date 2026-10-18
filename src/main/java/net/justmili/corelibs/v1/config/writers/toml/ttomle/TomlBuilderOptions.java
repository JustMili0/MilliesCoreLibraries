package net.justmili.corelibs.v1.config.writers.toml.ttomle;

public class TomlBuilderOptions {

    public boolean indentTables;
    public int indentAmount;

    public TomlBuilderOptions() {
        this.indentTables = true;
        this.indentAmount = 1;
    }

    public TomlBuilderOptions indentTables(boolean indentTables) {
        this.indentTables = indentTables;
        return this;
    }

    public TomlBuilderOptions indentAmount(int indentAmount) {
        this.indentAmount = indentAmount;
        return this;
    }

}
