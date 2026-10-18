package net.justmili.corelibs.libs.config.writers.toml.ttomle;

public abstract class TomlTableBase extends TomlElement {


    public TomlTableBase(TomlDottedElementName name) {
        this.name = name;
    }

    public int getTableDepth() {
        int depth = name.depth() - 1;
        if (depth < 1) return 0;
        return depth;
    }
}