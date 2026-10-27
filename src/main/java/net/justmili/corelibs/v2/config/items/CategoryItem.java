package net.justmili.corelibs.v2.config.items;

import java.util.ArrayList;
import java.util.List;

public final class CategoryItem implements ConfigItem {
    private final String name;
    private String comment;
    private final List<ConfigItem> children = new ArrayList<>();

    public CategoryItem(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public void comment(String comment) {
        this.comment = comment;
    }

    public String name() {
        return name;
    }

    public String comment() {
        return comment;
    }

    public void add(ConfigItem item) {
        children.add(item);
    }

    public List<ConfigItem> children() {
        return children;
    }
}