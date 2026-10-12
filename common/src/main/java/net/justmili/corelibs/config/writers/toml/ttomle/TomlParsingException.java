package net.justmili.corelibs.config.writers.toml.ttomle;

public class TomlParsingException extends Exception{

    public TomlParsingException(String message) {
        super(message);
    }

    public TomlParsingException(String message, String chars, int index) {
        super(message(message, chars, index));
    }

    public static String message(String message, String chars, int index) {
        String before = chars.substring(0, index);
        String[] lines = before.split("\n", -1);
        int lineNumber = lines.length;        // =1-based
        int column = lines[lines.length - 1].length(); // 0-based
        return message + "; error at index: " + index + " that is line: " + lineNumber + " column: " + column;
    }
}
