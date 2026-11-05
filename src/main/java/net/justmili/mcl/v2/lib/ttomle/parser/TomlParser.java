package net.justmili.mcl.v2.lib.ttomle.parser;

import net.justmili.mcl.v2.lib.ttomle.data.*;
import net.justmili.mcl.v2.lib.ttomle.elements.*;
import net.justmili.mcl.v2.lib.ttomle.elements.fields.*;
import net.justmili.mcl.v2.lib.ttomle.util.TomlStringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// todo: Inline tables, arrays(variable), date/time variable support, comment parsing (now just ignored)
// todo: fix escape handling, binary and hex support for ints

public class TomlParser {

    private final List<TomlElement> elements = new ArrayList<>();
    private final TomlContent content;

    public TomlParser(File tomlFile) {

        try {
            String rawFileContents = Files.readString(tomlFile.toPath());

            content = new TomlContent(rawFileContents);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public TomlParser(String rawFileContents) {
        content = new TomlContent(rawFileContents);
    }

    public void parse() throws TomlParsingException {

        boolean comment = false;
        boolean lineHasElement = false;

        while (content.getIndex() < content.contentSize) {

            if (content.current() == '\n') {
                content.advance();
                comment = false;
                lineHasElement = false;
                continue;
            }

            if (comment) {
                content.advance();
                continue;
            }

            if (content.current() == '#') {
                content.advance();
                comment = true;
                continue;
            }

            if (TomlStringUtils.isWhitespace(content.current())) {
                content.advance();
                continue;
            }

            if (content.current() == '[') {
                if (lineHasElement) {
                    throw new TomlParsingException("There must be a newline before the next element", content);
                }
                lineHasElement = true;
                content.advance();
                content.advanceThroughWhitespace();
                if (content.isAtFileEnd) {
                    throw new TomlParsingException("Array opened. but no name or closure.", content);
                }
                if (content.current() == '"' || TomlStringUtils.isCharInNoQuote(content.current())) {
                    TomlDottedElementName tableName = getDotSeparatedStringASCIElement();
                    content.advanceThroughWhitespace();

                    if (content.isAtFileEndOrLineBreak()) throw new TomlParsingException("Array not closed", content);

                    // get the end bracket
                    if (content.current() == ']') {
                        content.advance();
                        // add the element
                        TomlTable table = new TomlTable(tableName);
                        elements.add(table);
                        continue;
                    } else {
                        throw new TomlParsingException("Array not closed.", content);
                    }

                } else if (content.current() == '[') {
                    content.advance();

                    TomlDottedElementName tableName = getDotSeparatedStringASCIElement();

                    content.advanceThroughWhitespace();

                    // get the end bracket
                    if (content.current() == ']' && content.next() == ']') {
                        content.advance(2);
                        // add the element
                        TomlArrayTable table = new TomlArrayTable(tableName);
                        elements.add(table);
                    } else {
                        throw new TomlParsingException("Array Table not terminated", content);
                    }


                }
                // something like this needs to be in place to check for stray characters that are not supposed to be there
                // basically there needs to be a get trailing whitespace + check for comments and enters
                throw new TomlParsingException("Unrecognised character", content);
            }

            if (TomlStringUtils.isCharInNoQuote(content.current()) || content.current() == '"' || content.current() == '\'') {
                if (lineHasElement) {
                    throw new TomlParsingException("There must be a newline before the next element", content);
                }
                lineHasElement = true;
                TomlDottedElementName name = getDotSeparatedStringASCIElement();

                content.advanceThroughWhitespace();

                // equals sign
                if (content.current() == '=') {

                    content.advance();
                    content.advanceThroughWhitespace();
                    
                    if (content.isAtFileEnd) {
                        throw new TomlParsingException("OEF after equals sign. No valid field found.",content);
                    }

                    // boolean
                    if (content.nextCharactersExist(4) && content.chars.startsWith("true", content.getIndex())) {
                        TomlBooleanField field = new TomlBooleanField(name, true);
                        elements.add(field);
                        content.advance(4);
                        continue;
                    } else if (content.nextCharactersExist(5) && content.chars.startsWith("false", content.getIndex())) {
                        TomlBooleanField field = new TomlBooleanField(name, false);
                        elements.add(field);
                        content.advance(5);
                        continue;
                    }

                    // number
                    if (Character.isDigit(content.current()) || content.current() == '+'
                        || content.current() == '-') {
                        NumberResult result = getNumber(); // todo: migrate from result types as ints.
                        if (result.type == 0) {
                            TomlIntegerField field = new TomlIntegerField(name, (Integer) result.number);
                            elements.add(field);
                            continue;
                        }
                        if (result.type == 1) {
                            TomlDecimalField field = new TomlDecimalField(name, (Double) result.number);
                            if (result.hasExponent) {
                                field.setExponent(result.exponent);
                            }
                            elements.add(field);
                            continue;
                        }
                    }

                    // normal and literal strings
                     if (content.current() == '"') {
                        TomlString stringValue = getDoubleQuotedStringElementWithTriples();
                        TomlStringField field = new TomlStringField(name, stringValue);
                        elements.add(field);
                        continue;
                    }
                     if (content.current() == '\'') {
                        TomlString stringValue = getLiteralStringElement();
                        TomlStringField field = new TomlStringField(name, stringValue);
                        elements.add(field);
                        continue;
                    }
                    // array
                    if (content.current() == '[') {
                        ArrayValueResult arrayValueResult = getArrayElement();
                        TomlArrayField field = new TomlArrayField(name, arrayValueResult.elements);
                        elements.add(field);
                        continue;
                    }

                    throw new TomlParsingException("TOML not followed by valid field", content);
                }
            }

            throw new TomlParsingException("Unrecognised Character: '" + Character.toString(content.current()) + "'", content);

        }
    }


    private record ArrayValueResult(List<TomlVariable> elements, int length) {
    }

    private ArrayValueResult getArrayElement() {
        return null;// not yet implemented
    }

    private TomlDottedElementName getDotSeparatedStringASCIElement() throws TomlParsingException {

        TomlDottedElementName element = new TomlDottedElementName();

        boolean trailingDot = false;
        boolean shouldDotOrEnd = false;

        while (!content.isAtFileEnd) {

            int character = content.current();

            if (character == '\n') {
                throw new TomlParsingException("You cannot have a newline in the middle of a Name", content);
            }

            if (TomlStringUtils.isWhitespace(character)) {
                content.advance();
                continue;
            }

            if (character == '"') {
                TomlString result = getDoubleQuotedStringElement();
                element.addName(result.value, result.type);
                shouldDotOrEnd = true;
                trailingDot = false;
                continue;
            }

            if (character == '\'') {
                TomlString result = getLiteralStringElement();
                element.addName(result.value, result.type);
                shouldDotOrEnd = true;
                trailingDot = false;
                continue;
            }

            if (TomlStringUtils.isCharInNoQuote(content.current())) {

                String result = getNoQuotedStringElement();

                element.addName(result, TomlStringType.NO_QUOTE);
                shouldDotOrEnd = true;
                trailingDot = false;
                continue;
            }

            if (character == '.') {
                if (trailingDot) {
                    throw new TomlParsingException("Two dots without content in between", content);
                }
                if (!shouldDotOrEnd) {
                    throw new TomlParsingException("Should expect dot here...", content);
                }
                shouldDotOrEnd = false;
                trailingDot = true;
                content.advance();
                continue;
            }

            if (character == '=' || character == ']') {
                if (trailingDot) {
                    throw new TomlParsingException("Dot trailing without content", content);
                }
                if (!shouldDotOrEnd) {
                    throw new TomlParsingException("Expected a name before this character", content);
                }
                break;
            }
            throw new TomlParsingException("Unexpected character in name", content);

        }

        return element;
    }

    private TomlString getDoubleQuotedStringElement() throws TomlParsingException {
        return getDoubleQuotedStringElement(false);
    }

    private TomlString getDoubleQuotedStringElementWithTriples() throws TomlParsingException {
        return getDoubleQuotedStringElement(true);
    }

    private TomlString getDoubleQuotedStringElement(boolean allowTriples) throws TomlParsingException {
        if (content.getIndex() < content.contentSize - 2 && allowTriples) {
            int firstQuote = content.current();
            int secondQuote = content.next();
            int thirdQuote = content.next(2);

            if (firstQuote == '"' && secondQuote == '"' && thirdQuote == '"') {
                return getTripleDoubleQuotedStringElement();
            }
        }
        StringBuilder string = new StringBuilder();
        content.advance();
        while (!content.isAtFileEnd) {

            if (content.current() == '\\') {
                String escape = getEscape();
                string.append(escape);
                continue;
            }

            if (content.current() == '\n') {
                throw new TomlParsingException("String not finished before line break", content);
            }

            if (content.current() == '\"') {
                content.advance();
                return new TomlString(string.toString(), TomlStringType.DOUBLE);
            }

            string.append(Character.toString(content.current()));
            content.advance();
        }
        throw new TomlParsingException("String was not terminated with ' \" '", content);
    }

    private TomlString getLiteralStringElement() throws TomlParsingException {
        StringBuilder string = new StringBuilder();
        content.advance();
        while(!content.isAtFileEndOrLineBreak()) {
            if (content.current() == '\n') {
                throw new TomlParsingException("String not finished before line break", content);
            }

            if (content.current() == '\'') {
                content.advance();
                return new TomlString(string.toString(), TomlStringType.LITERAL);
            }

            string.append(Character.toString(content.current()));
            content.advance();
        }
        throw new TomlParsingException("String was not terminated with ' \' '", content);
    }

    // todo: add support for line ending backslash
    private TomlString getTripleDoubleQuotedStringElement() throws TomlParsingException {

        // todo finish this
        if (!content.nextCharactersExist(3)) {
            throw new TomlParsingException("Triple quote not detected even though function if called", content);
        }
        int firstQuote = content.current();
        int secondQuote = content.next();
        int thirdQuote = content.next(2);

        if (firstQuote == '"' && secondQuote == '"' && thirdQuote == '"') {
            content.advance(3);
        } else {
            throw new TomlParsingException("There is no starting triple quote even through fuction is called", content);
        }
        StringBuilder builder = new StringBuilder();

        while (content.nextCharactersExist(2)) {

            if (content.current() == '"') {

                if (content.next() == '"' && content.next(2) == '"') {

                    // detect if there are extra double quotes. Those should get detected.
                    int quoteCount = 0;
                    while (content.current() == '"') {
                        content.advance();
                        quoteCount++;
                    }

                    String extraQuotes = "\"".repeat(quoteCount-3);
                    builder.append(extraQuotes);
                    return new TomlString(builder.toString(), TomlStringType.TRIPLE_DOUBLE);
                }
            }
            builder.append(Character.toString(content.current()));
            content.advance();
        }

        throw new TomlParsingException("No closing '\"\"\"' found within content", content);
    }

    private String getNoQuotedStringElement() throws TomlParsingException {

        StringBuilder string = new StringBuilder();

        while(TomlStringUtils.isCharInNoQuote(content.current()) && !content.isAtFileEndOrLineBreak()) {
            string.append(Character.toString(content.current()));
            content.advance();
        }
        return string.toString();

    }

    private String getEscape() throws TomlParsingException {
        switch (content.next()) {
            case 'n':
                content.advance(2);
                return "\n";
            case 'b':
                content.advance(2);
                return "\b";
            case 't':
                content.advance(2);
                return "\t";
            case 'f':
                content.advance(2);
                return "\f";
            case 'r':
                content.advance(2);
                return "\r";
            case 'e':
                content.advance(2);
                return "\u001B";
            case '"':
                content.advance(2);
                return "\"";
            case '\\':
                content.advance(2);
                return "\\";
            case 'x':
                if (content.nextCharactersExist(3)) {
                    throw new TomlParsingException("Unicode escape character not finished", content);
                }
                // parse the Unicode
                String hex2 = content.chars.substring(content.getIndex() + 2, content.getIndex() + 5);
                try {
                    int codePoint = Integer.parseInt(hex2, 16);
                    // EscapeResult should accept int codepoint, not char
                    content.advance(4);
                    return Character.toString(codePoint);
                } catch (NumberFormatException e) {
                    throw new TomlParsingException("Invalid Unicode escape: " + hex2, content);
                }
            case 'u':
                if (content.nextCharactersExist(5)) {
                    throw new TomlParsingException("Unicode escape character not finished", content);
                }
                // parse the Unicode
                String hex4 = content.chars.substring(content.getIndex() + 2, content.getIndex() + 7);
                try {
                    int codePoint = Integer.parseInt(hex4, 16);
                    // EscapeResult should accept int codepoint, not char
                    content.advance(6);
                    return Character.toString(codePoint);
                } catch (NumberFormatException e) {
                    throw new TomlParsingException("Invalid Unicode escape: " + hex4, content);
                }
            case 'U':
                if (content.nextCharactersExist(9)) {
                    throw new TomlParsingException("Unicode escape character not finished, chars, index", content);
                }
                // parse the Unicode
                String hex8 = content.chars.substring(content.getIndex() + 2, content.getIndex() + 11);
                try {
                    int codePoint = Integer.parseInt(hex8, 16);
                    // EscapeResult should accept int codepoint, not char
                    content.advance(10);
                    return Character.toString(codePoint);
                } catch (NumberFormatException e) {
                    throw new TomlParsingException("Invalid Unicode escape: " + hex8, content);
                }
            default:
                throw new TomlParsingException("Invalid character escape: " + content.next(), content);
        }
    }

    private record NumberResult(Number number, int exponent, boolean hasExponent, int type) {
    }

    private NumberResult getNumber() throws TomlParsingException {

        // the first character must b

        boolean dotPresent = false;
        boolean numberAfter = false;
        boolean hasExponent = false;
        boolean allowedPlus = true;

        StringBuilder mainNumberString = new StringBuilder();
        StringBuilder exponentString = new StringBuilder();


        while (!content.isAtFileEndOrLineBreak()) {

             if (Character.isDigit(content.current())) {
                numberAfter = false;
                allowedPlus = false;

                if (hasExponent) {
                    exponentString.append(Character.toString(content.current()));
                } else {
                    mainNumberString.append(Character.toString(content.current()));
                }
                content.advance();
                continue;
            }

            if (content.current() == '.') {
                if (dotPresent) {
                    throw new TomlParsingException("a number cannot contain two dots!", content);
                }
                if (hasExponent) {
                    throw new TomlParsingException("a number cannot contain a dot in the exponent!", content);
                }
                dotPresent = true;
                numberAfter = true;
                mainNumberString.append(Character.toString(content.current()));
                content.advance();
                continue;
            }

            if (content.current() == 'e' || content.current() == 'E') {
                if (numberAfter) {
                    throw new TomlParsingException("TOML requires a number after a dot or exponent", content);
                }

                // exponent
                numberAfter = true;
                hasExponent = true;
                allowedPlus = true;
                content.advance();
                continue;
            }
            if (content.current() == '+') {

                if (!allowedPlus) {
                    throw new TomlParsingException("A \"+\" is not allowed here!", content);
                }

                content.advance();
                continue;
            }

            if (content.current() == '-') {
                if (!allowedPlus) {
                    throw new TomlParsingException("A \"-\" is not allowed here!", content);
                }

                if (hasExponent) {
                    exponentString.append('-');
                } else {
                    mainNumberString.append('-');
                }
                content.advance();
                continue;
            }

            if (Character.isWhitespace(content.current())) {
                break;
            }

            throw new TomlParsingException("This character is not allowed in a digit", content);


        }

        if (numberAfter) {
            throw new TomlParsingException("TOML requires a number after a dot or exponent", content);
        }

        int exponent = 0;

        if (hasExponent) {
            exponent = Integer.parseInt(exponentString.toString());
        }

        if (dotPresent) {
            return new NumberResult(Double.parseDouble(mainNumberString.toString()), exponent, hasExponent, 1);
        }

        return new NumberResult(Integer.parseInt(mainNumberString.toString()), exponent, hasExponent, 0);

    }

    public List<TomlElement> getElements() {
        return elements;
    }

}