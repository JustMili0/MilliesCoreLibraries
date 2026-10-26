package net.justmili.corelibs.v2.config.writers.toml.ttomle;

import java.util.ArrayList;
import java.util.List;

public abstract class TomlElement {

    public TomlDottedElementName name;

    public String inlineComment;
    public List<String> headerComments;

    protected TomlElement() {
        this.inlineComment = null;
        this.headerComments = new ArrayList<>();
    }

    public void setInlineComment(String comment) {
        this.inlineComment = comment;
    }

    public void setHeaderComments(List<String> headerComments) {
        this.headerComments = headerComments;
    }

    public void addHeaderComment(String comment) {
        this.headerComments.add(comment);
    }
}