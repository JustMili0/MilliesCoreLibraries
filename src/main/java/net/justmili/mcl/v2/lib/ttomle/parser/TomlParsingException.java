package net.justmili.mcl.v2.lib.ttomle.parser;

public class TomlParsingException extends Exception {

    public TomlParsingException(String message) {
        super(message);
    }

    public TomlParsingException(String message, TomlContent content) {
        super(message(message, content));
    }

    public static String message(String message, TomlContent content) {
        String before;
        if (content.isAtFileEnd) {
            before = content.chars.substring(0, content.getIndex() - 1);
        } else {
            before = content.chars.substring(0, content.getIndex());
        }
        String[] lines = before.split("\n", -1);
        int lineNumber = lines.length;        // =1-based
        int column = lines[lines.length - 1].length(); // 0-based
        return message + "; error at index: " + content.getIndex() + " that is line: " + lineNumber + " column: " + column;
    }
}
