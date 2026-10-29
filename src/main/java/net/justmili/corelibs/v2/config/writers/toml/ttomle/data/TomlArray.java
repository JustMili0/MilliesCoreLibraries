package net.justmili.corelibs.v2.config.writers.toml.ttomle.data;

import net.justmili.corelibs.v2.config.writers.toml.ttomle.TomlBuildingException;

import java.util.List;

public class TomlArray extends TomlVariable {

    public List<TomlVariable> variableList;

    public TomlArray(List<TomlVariable> variableList) {
        this.variableList = variableList;
    }

    @Override
    public String toString() {
        if (variableList.contains(this)) {
            try {
                throw new TomlBuildingException("TOML array can't contain itself!");
            } catch (TomlBuildingException e) {
                throw new RuntimeException(e);
            }
        }
        if (variableList.isEmpty()) return "[]";
        if (variableList.size() == 1) return "[" + variableList.get(0).toString() + "]";

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(variableList.get(0).toString());

        for (int i = 1; i < variableList.size(); i++) {
            builder.append(", ");
            TomlVariable variable = variableList.get(i);
            builder.append(variable.toString());
        }
        builder.append("]");
        return builder.toString();
    }

}
