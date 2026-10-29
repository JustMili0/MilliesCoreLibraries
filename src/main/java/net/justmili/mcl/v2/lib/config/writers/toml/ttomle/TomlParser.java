package net.justmili.mcl.v2.lib.config.writers.toml.ttomle;

import net.justmili.mcl.v2.lib.config.writers.toml.ttomle.data.TomlVariable;
import net.justmili.corelibs.v2.config.writers.toml.ttomle.fields.*;
import net.justmili.mcl.v2.config.writers.toml.ttomle.fields.*;
import net.justmili.mcl.v2.lib.config.writers.toml.ttomle.fields.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TomlParser {

    private final List<TomlElement> elements = new ArrayList<>();

    public TomlParser(File tomlFile) throws TomlParsingException {

        try (FileReader reader = new FileReader(tomlFile)) {
            String everything = IOUtils.toString(reader);

            parseToml(everything);
            // do something with everything string
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TomlParser(String rawFileContents) throws TomlParsingException {
        parseToml(rawFileContents);
    }

    private void parseToml(String tomlContent) throws TomlParsingException {

        int index = 0;
        boolean comment = false;
        boolean lineHasElement = false;
        while (index < tomlContent.length()) {

            if (tomlContent.codePointAt(index) == '\n') {
                index += 1;
                comment = false;
                lineHasElement = false;
                continue;
            }

            if (comment) {
                index += 1;
                continue;
            }

            if (tomlContent.codePointAt(index) == '#') {
                index += 1;
                comment = true;
                continue;
            }

            if (Character.isWhitespace(tomlContent.codePointAt(index))) {
                index += 1;
                continue;
            }

            if (tomlContent.codePointAt(index) == '[') {
                if (lineHasElement) {
                    throw new TomlParsingException("There must be a newline before the next element", tomlContent, index);
                }
                lineHasElement = true;
                index += 1;
                index += getTrailingWhitespace(tomlContent, index);
                if (index >= tomlContent.length()) {
                    throw new TomlParsingException("Array opened. but no name or closure.", tomlContent, index);
                }
                if (tomlContent.codePointAt(index) == '"' || TomlStringUtils.isCharInNoQuote(tomlContent.codePointAt(index))) {
                    TomlDottedElementResult result = getDotSeparatedStringASCIElement(tomlContent, index);
                    index += result.length;

                    if (index >= tomlContent.length()) throw new TomlParsingException("Array not closed. Random EOF", tomlContent, index);

                    // ignore whitespace again
                    index += getTrailingWhitespace(tomlContent, index);

                    if (index >= tomlContent.length()) throw new TomlParsingException("Array not closed. Random EOF", tomlContent, index);

                    // get the end bracket
                    if (tomlContent.codePointAt(index) == ']') {
                        index += 1;
                        // add the element
                        TomlTable table = new TomlTable(result.name);
                        elements.add(table);
                    } else {
                        throw new TomlParsingException("Array not closed.", tomlContent, index);
                    }

                    index += getTrailingWhitespace(tomlContent, index);

                } else if (tomlContent.codePointAt(index) == '[') {
                    index += 1;

                    TomlDottedElementResult result = getDotSeparatedStringASCIElement(tomlContent, index);
                    index += result.length;

                    // ignore whitespace again
                    index += getTrailingWhitespace(tomlContent, index);

                    // get the end bracket
                    if (tomlContent.codePointAt(index) == ']' && tomlContent.codePointAt(index + 1) == ']') {
                        index += 2;
                        // add the element
                        TomlArrayTable table = new TomlArrayTable(result.name);
                        elements.add(table);
                    }
                    index += getTrailingWhitespace(tomlContent, index);

                }
                continue;
            }

            char c = tomlContent.charAt(index);
            if (TomlStringUtils.isCharInNoQuote(c) || c == '"') {
                if (lineHasElement) {
                    throw new TomlParsingException("There must be a newline before the next element", tomlContent, index);
                }
                lineHasElement = true;
                TomlDottedElementResult nameResult = getDotSeparatedStringASCIElement(tomlContent, index);
                index += nameResult.length;

                index += getTrailingWhitespace(tomlContent, index);

                // equals sign
                if (tomlContent.codePointAt(index) == '=') {

                    index += 1;
                    index += getTrailingWhitespace(tomlContent, index);

                    // boolean
                    if (tomlContent.startsWith("true", index)) {
                        TomlBooleanField field = new TomlBooleanField(nameResult.name, true);
                        elements.add(field);
                        index += 4;
                    } else if (tomlContent.startsWith("false", index)) {
                        TomlBooleanField field = new TomlBooleanField(nameResult.name, false);
                        elements.add(field);
                        index += 5;
                    }

                    // number
                    if (Character.isDigit(tomlContent.codePointAt(index)) || tomlContent.codePointAt(index) == '+'
                        || tomlContent.codePointAt(index) == '-') {
                        NumberResult result = getNumber(tomlContent, index);
                        if (result.type == 0) {
                            TomlIntegerField field = new TomlIntegerField(nameResult.name, (Integer) result.number);
                            if (result.hasExponent) {
                                //field.setExponent(result.exponent); todo: do this better
                            }
                            elements.add(field);
                        }
                        if (result.type == 1) {
                            TomlDecimalField field = new TomlDecimalField(nameResult.name, (Double) result.number);
                            if (result.hasExponent) {
                                field.setExponent(result.exponent);
                            }
                            elements.add(field);
                        }
                        index += result.length;
                    }

                    // normal and literal strings
                    else if (tomlContent.codePointAt(index) == '"') {
                        StringFieldValueResult stringValueResult = getDoubleQuotedStringElementWithTriples(tomlContent, index);
                        TomlStringField field = new TomlStringField(nameResult.name, stringValueResult.character, stringValueResult.type);
                        elements.add(field);
                        index += stringValueResult.length;
                    } else if (tomlContent.codePointAt(index) == '\'') {
                        StringResult stringValueResult = getLiteralStringElement(tomlContent, index);
                        TomlStringField field = new TomlStringField(nameResult.name, stringValueResult.character, TomlStringType.LITERAL);
                        elements.add(field);
                        index += stringValueResult.length;
                    }
                    // array
                    else if (tomlContent.codePointAt(index) == '[') {
                        ArrayValueResult arrayValueResult = getArrayElement(tomlContent, index);
                        TomlArrayField field = new TomlArrayField(nameResult.name, arrayValueResult.elements);
                        elements.add(field);
                        index += arrayValueResult.length;
                    }


                }
                index += getTrailingWhitespace(tomlContent, index);

            }

        }
    }


    private record ArrayValueResult(List<TomlVariable> elements, int length) {
    }

    private ArrayValueResult getArrayElement(String tomlContent, int index) {
        return null;
    }

    private int getTrailingWhitespace(String chars, int index) {
        if (chars.isEmpty()) {
            return 0;
        }
        if (index >= chars.length()) {
            return 0;
        }
        if (index < 0) {
            return 0;
        }
        int whitespace = 0;
        for (int i = index; i < chars.length(); i++) {
            if (Character.isWhitespace(chars.codePointAt(i)) && chars.codePointAt(i) != '\n') {
                whitespace += 1;
            } else {
                break;
            }
        }
        return whitespace;
    }

    public record TomlDottedElementResult(TomlDottedElementName name, int length) {
    }

    private TomlDottedElementResult getDotSeparatedStringASCIElement(String chars, int index) throws TomlParsingException {

        TomlDottedElementName element = new TomlDottedElementName();

        int endIndex = -1;
        boolean trailingDot = false;
        boolean shouldDotOrEnd = false;
        int i = index;

        while (index < chars.length()) {

            int character = chars.codePointAt(i);

            if (character == '\n') {
                throw new TomlParsingException("You cannot have a newline in the middle of a Name", chars, i);
            }

            if (Character.isWhitespace(character)) {
                i++;
                continue;
            }

            if (character == '"') {
                StringFieldValueResult result = getDoubleQuotedStringElement(chars, i);
                element.addName(result.character, result.type);
                i += result.length();
                shouldDotOrEnd = true;
                trailingDot = false;
                continue;
            }

            if (TomlStringUtils.isCharInNoQuote(chars.codePointAt(i))) {

                StringResult result = getNoQuotedStringElement(chars, i);

                if (result == null) {
                    throw new TomlParsingException("Failed to get no quoted string element", chars, i);
                }

                element.addName(result.character, TomlStringType.NO_QUOTE);
                i += result.length;
                shouldDotOrEnd = true;
                trailingDot = false;
                continue;
            }

            if (character == '.') {
                if (!shouldDotOrEnd) {
                    throw new TomlParsingException("Should expect dot here...", chars, i);
                }
                if (trailingDot) {
                    throw new TomlParsingException("Two dots without content in between", chars, i);
                }
                shouldDotOrEnd = false;
                trailingDot = true;
                i += 1;
                continue;
            }

            if (character == '=' || character == ']') {
                if (!shouldDotOrEnd) {
                    throw new TomlParsingException("Should expect equals sign here...", chars, i);
                }
                shouldDotOrEnd = false;
                // break normally
                if (trailingDot) {
                    throw new TomlParsingException("Dot trailing without content", chars, i);
                }
                endIndex = i;
                break;
            }

        }

        return new TomlDottedElementResult(element, endIndex - index);
    }

    private StringFieldValueResult getDoubleQuotedStringElement(String chars, int index) throws TomlParsingException {
        return getDoubleQuotedStringElement(chars, index, false);
    }

    private record StringFieldValueResult(String character, int length, TomlStringType type) {
    }

    private StringFieldValueResult getDoubleQuotedStringElementWithTriples(String chars, int index) throws TomlParsingException {
        return getDoubleQuotedStringElement(chars, index, true);
    }

    private StringFieldValueResult getDoubleQuotedStringElement(String chars, int index, boolean allowTriples) throws TomlParsingException {
        if (chars.isEmpty()) {
            return null;
        }
        if (index >= chars.length()) {
            return null;
        }
        if (index < 0) {
            throw new TomlParsingException("Index on getDoubleQuotedStingElement negative", chars, index);
        }
        if (index < chars.length() - 2 && allowTriples) {
            int firstQuote = chars.codePointAt(index);
            int secondQuote = chars.codePointAt(index + 1);
            int thirdQuote = chars.codePointAt(index + 2);

            if (firstQuote == '"' && secondQuote == '"' && thirdQuote == '"') {
                index += 3;
                return getTripleDoubleQuotedStringElement(chars, index);
            }
        }
        StringBuilder string = new StringBuilder();
        int i = index + 1;
        while (i < chars.length()) {

            if (chars.codePointAt(i) == '\\') {
                StringResult escape = getEscape(chars, i);
                i += escape.length;
                string.append(escape.character);
                continue;
            }

            if (chars.codePointAt(i) == '\n') {
                throw new TomlParsingException("String not finished before line break", chars, index);
            }

            if (chars.codePointAt(i) == '\"') {
                break;
            }

            string.append(Character.toString(chars.codePointAt(i)));
            i++;
        }
        return new StringFieldValueResult(string.toString(), i - index + 1, TomlStringType.DOUBLE);
    }

    private StringResult getLiteralStringElement(String chars, int index) throws TomlParsingException {
        if (chars.isEmpty()) {
            throw new TomlParsingException("There is no toml...", chars, index);
        }
        if (index >= chars.length()) {
            throw new TomlParsingException("Index out of range on getLiteralStringElement", chars, index);
        }
        if (index < 0) {
            throw new TomlParsingException("Index on getLiteralStringElement negative", chars, index);
        }
        StringBuilder string = new StringBuilder();
        for (int i = index + 1; i < chars.length(); i++) {
            if (chars.codePointAt(i) == '\n') {
                throw new TomlParsingException("String not finished before line break", chars, index);
            }

            if (chars.codePointAt(i) == '\'') {
                break;
            }

            string.append(Character.toString(chars.codePointAt(i)));
        }
        return new StringResult(string.toString(), index + 1);
    }

    private StringFieldValueResult getTripleDoubleQuotedStringElement(String chars, int index) throws TomlParsingException {

        // todo finish this
        if (index >= chars.length() - 2) {
            throw new TomlParsingException("Triple quote not detected", chars, index);
        }

        int firstQuote = chars.codePointAt(index);
        int secondQuote = chars.codePointAt(index + 1);
        int thirdQuote = chars.codePointAt(index + 2);

        if (firstQuote == '"' && secondQuote == '"' && thirdQuote == '"') {
            index += 3;
        } else {
            return null;
        }

        int i = index;
        StringBuilder builder = new StringBuilder();

        while (i < chars.length()) {

            if (chars.codePointAt(i) == '"') {

                if (chars.codePointAt(index + 1) == '"' && chars.codePointAt(index + 2) == '"') {
                    index += 3;

                    int quoteCount = 3;
                    while (i + quoteCount < chars.length() && chars.codePointAt(index + quoteCount) == '"') {
                        quoteCount++;
                    }

                    String extraQuotes = "\"".repeat(quoteCount);
                    builder.append(extraQuotes);

                    break;
                }
            }
            builder.append(chars.codePointAt(i));
            index += 1;
        }

        return new StringFieldValueResult(builder.toString(), builder.length(), TomlStringType.TRIPLE_DOUBLE);
    }

    private StringResult getNoQuotedStringElement(String chars, int index) throws TomlParsingException {

        if (chars.isEmpty()) {
            return null;
        }
        if (index >= chars.length()) {
            return null;
        }
        if (index < 0) {
            throw new TomlParsingException("Index on getDoubleQuotedStingElement negative", chars, index);
        }
        StringBuilder string = new StringBuilder();
        for (int i = index; i < chars.length(); i++) {

            int character = chars.codePointAt(i);

            if (TomlStringUtils.isCharInNoQuote(character)) {

                string.append(Character.toString(character));

            } else {
                break;
            }
        }
        return new StringResult(string.toString(), string.length());

    }

    private record StringResult(String character, int length) {
    }

    private StringResult getEscape(String chars, int index) throws TomlParsingException {
        if (index < 0) {
            throw new TomlParsingException("Index of escape character check negative", chars, index);
        }
        switch (chars.codePointAt(index + 1)) {
            case 'n':
                return new StringResult("\n", 2);
            case 'b':
                return new StringResult("\b", 2);
            case 't':
                return new StringResult("\t", 2);
            case 'f':
                return new StringResult("\f", 2);
            case 'r':
                return new StringResult("\r", 2);
            case 'e':
                return new StringResult("\u001B", 2);
            case '"':
                return new StringResult("\"", 2);
            case '\\':
                return new StringResult("\\", 2);
            case 'x':
                if (index >= chars.length() - 3) {
                    throw new TomlParsingException("Unicode escape character not finished", chars, index);
                }
                // parse the Unicode
                String hex2 = chars.substring(index + 2, index + 5);
                try {
                    int codePoint = Integer.parseInt(hex2, 16);
                    // EscapeResult should accept int codepoint, not char
                    return new StringResult(Character.toString(codePoint), 4);
                } catch (NumberFormatException e) {
                    throw new TomlParsingException("Invalid Unicode escape: " + hex2, chars, index);
                }
            case 'u':
                if (index >= chars.length() - 5) {
                    throw new TomlParsingException("Unicode escape character not finished", chars, index);
                }
                // parse the Unicode
                String hex4 = chars.substring(index + 2, index + 7);
                try {
                    int codePoint = Integer.parseInt(hex4, 16);
                    // EscapeResult should accept int codepoint, not char
                    return new StringResult(Character.toString(codePoint), 6);
                } catch (NumberFormatException e) {
                    throw new TomlParsingException("Invalid Unicode escape: " + hex4, chars, index);
                }
            case 'U':
                if (index >= chars.length() - 9) {
                    throw new TomlParsingException("Unicode escape character not finished, chars, index", chars, index);
                }
                // parse the Unicode
                String hex8 = chars.substring(index + 2, index + 11);
                try {
                    int codePoint = Integer.parseInt(hex8, 16);
                    // EscapeResult should accept int codepoint, not char
                    return new StringResult(Character.toString(codePoint), 10);
                } catch (NumberFormatException e) {
                    throw new TomlParsingException("Invalid Unicode escape: " + hex8, chars, index);
                }
            default:
                throw new TomlParsingException("Invalid character escape: " + chars.codePointAt(index + 1), chars, index);
        }
    }

    private record NumberResult(Number number, int exponent, boolean hasExponent, int type, int length) {
    }

    private NumberResult getNumber(String chars, int index) throws TomlParsingException {
        if (chars.isEmpty()) {
            return null;
        }
        if (index >= chars.length()) {
            return null;
        }
        if (index < 0) {
            throw new TomlParsingException("Index on getNumber negative", chars, index);
        }

        // the first character must b

        int i = index;
        boolean dotPresent = false;
        boolean numberAfter = false;
        boolean hasExponent = false;
        boolean allowedPlus = true;

        StringBuilder mainNumberString = new StringBuilder();
        StringBuilder exponentString = new StringBuilder();


        while (i < chars.length()) {

            if (Character.isDigit(chars.codePointAt(i))) {
                numberAfter = false;
                allowedPlus = false;

                if (hasExponent) {
                    exponentString.append(Character.toString(chars.codePointAt(i)));
                } else {
                    mainNumberString.append(Character.toString(chars.codePointAt(i)));
                }
                i++;
                continue;
            }

            if (chars.codePointAt(i) == '.') {
                if (dotPresent) {
                    throw new TomlParsingException("a number cannot contain two dots!", chars, i);
                }
                if (hasExponent) {
                    throw new TomlParsingException("a number cannot contain a dot in the exponent!", chars, i);
                }
                dotPresent = true;
                numberAfter = true;
                mainNumberString.append(Character.toString(chars.codePointAt(i)));
                i++;
                continue;
            }

            if (chars.codePointAt(i) == 'e' || chars.codePointAt(i) == 'E') {
                if (numberAfter) {
                    throw new TomlParsingException("TOML requires a number after a dot or exponent", chars, i);
                }

                // exponent
                numberAfter = true;
                hasExponent = true;
                allowedPlus = true;
                i++;
                continue;
            }
            if (chars.codePointAt(i) == '+') {

                if (!allowedPlus) {
                    throw new TomlParsingException("A \"+\" is not allowed here!", chars, i);
                }

                i++;
                continue;
            }

            if (chars.codePointAt(i) == '-') {
                if (!allowedPlus) {
                    throw new TomlParsingException("A \"-\" is not allowed here!", chars, i);
                }

                if (hasExponent) {
                    exponentString.append('-');
                } else {
                    mainNumberString.append('-');
                }
                i++;
                continue;
            }


            throw new TomlParsingException("This character is not allowed in a digit", chars, i);


        }

        if (numberAfter) {
            throw new TomlParsingException("TOML requires a number after a dot or exponent", chars, index);
        }

        int exponent = 0;

        if (hasExponent) {
            exponent = Integer.parseInt(exponentString.toString());
        }

        if (dotPresent) {
            return new NumberResult(Double.parseDouble(mainNumberString.toString()), exponent, hasExponent, 1, i - index);
        }

        return new NumberResult(Integer.parseInt(mainNumberString.toString()), exponent, hasExponent, 0, i - index);

    }

    public List<TomlElement> getElements() {
        return elements;
    }

}