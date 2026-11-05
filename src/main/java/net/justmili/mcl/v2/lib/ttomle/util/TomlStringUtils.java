package net.justmili.mcl.v2.lib.ttomle.util;

import net.justmili.mcl.v2.lib.ttomle.data.TomlStringType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TomlStringUtils {

    public static String escapeTomlString(String value) {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }

    public static String escapeTomlTripleDoubleString(String value) {
        return value.replace("\"\"\"", "\\\"\\\"\\\"");
    }

    public static boolean isLiteralStringValid(String literal) {
        return !literal.contains("'") && !literal.contains("\n") && !literal.contains("\r");
    }

    public static boolean isNoQuoteStringValid(String noQuote) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9_-]");
        Matcher matcher = pattern.matcher(noQuote);
        return matcher.matches();
    }

    public static boolean isCharInNoQuote(int codePoint) {
        return Character.isLetterOrDigit(codePoint) || codePoint == '_' || codePoint == '-';
    }

    // check if this is whitespace i.e. a tab or a string;
    public static boolean isWhitespace(int codePoint) {
        return Character.isWhitespace(codePoint) && codePoint != '\n';
    }

    public static TomlStringType getPreferredStringType(String string) {
        if (isNoQuoteStringValid(string)) {
            return TomlStringType.NO_QUOTE;
        } else {
            return TomlStringType.DOUBLE;
        }
    }
}
