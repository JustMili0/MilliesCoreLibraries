package net.justmili.corelibs.v2.config.writers.toml.ttomle;

import net.justmili.corelibs.v2.config.writers.toml.ttomle.fields.TomlField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TomlBuilder {

    private List<TomlElement> elements;
    private TomlBuilderOptions options;


    public TomlBuilder(TomlBuilderOptions options) {
        this.elements = new ArrayList<>();
        this.options = options;
    }

    public TomlBuilder addElement(TomlElement element) {
        this.elements.add(element);
        return this;
    }

    public String build() throws TomlParsingException {
        StringBuilder sb = new StringBuilder();
        TomlTableBase tomlTable = null;
        Set<TomlDottedElementName> tableNames = new HashSet<>();

        for (TomlElement element : this.elements) {

            if (element instanceof TomlTable) {
                if (tableNames.contains(((TomlTable) element).name)) {
                    throw new TomlParsingException("Two tables cannot have the same name!");
                }
                tableNames.add(((TomlTable) element).name);
            }
            if (element instanceof TomlTableBase) {
                if (!sb.isEmpty()) {
                    sb.append("\n");
                }
                tomlTable = (TomlTableBase) element;
                String indent = "";

                // Header Comments
                for (String comment: element.headerComments) {
                    sb.append(indent).append("# ").append(comment).append("\n");
                }

                if (options.indentTables) {
                    int depth = tomlTable.getTableDepth();
                    if (depth > 0) {
                        indent = " ".repeat(depth * options.indentAmount);
                    }
                }
                sb.append(indent).append(tomlTable);

                // Inline Comment
                if (element.inlineComment != null) {
                    sb.append(" # ").append(element.inlineComment);
                }
            }
            if (element instanceof TomlField) {
                // figure out the indent if applicable
                String indent = "";
                if (tomlTable != null && options.indentTables) {
                    int depth = tomlTable.getTableDepth();
                    if (depth > 0) {
                        indent = " ".repeat(depth * options.indentAmount);
                    }
                }

                // Header Comments
                for (String comment: element.headerComments) {
                    sb.append(indent).append("# ").append(comment).append("\n");
                }

                sb.append(indent).append(element);

                // Inline Comment
                if (element.inlineComment != null) {
                    sb.append(" # ").append(element.inlineComment);
                }
            }
            sb.append('\n');
        }
        
        return sb.toString();
    }

}
