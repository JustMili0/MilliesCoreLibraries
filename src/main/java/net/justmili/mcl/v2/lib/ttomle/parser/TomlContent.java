package net.justmili.mcl.v2.lib.ttomle.parser;

import net.justmili.mcl.v2.lib.ttomle.util.TomlStringUtils;

public class TomlContent {

    public final String chars;
    private int index = 0;
    public final int contentSize;

    public boolean isAtFileEnd;

    public TomlContent(String chars) {
        this.chars = chars;
        contentSize = chars.length();
        isAtFileEnd = chars.isEmpty();
    }

    public void advance() throws TomlParsingException {
        index += 1;
        if (index == contentSize) {
            isAtFileEnd = true;
        }
        if (index > contentSize) {
            throw new TomlParsingException("Index out of upper bound");
        }
    }

    public void previous() throws TomlParsingException {
        index -= 1;

        if (index < 0) {
            throw new TomlParsingException("Index out of lower bound");
        }
    }

    public void advance(int amount) throws TomlParsingException {
        index += amount;
        if (index == contentSize) {
            isAtFileEnd = true;
        }
        if (index > contentSize) {
            throw new TomlParsingException("Index out of upper bound");
        }
        if (index < 0) {
            throw new TomlParsingException("Index out of lower bound");
        }
    }

    // get the next whitespace and advances the index to the next non whitespace character
    // whitespace contains spaces and tabs but not line breaks
    public int advanceThroughWhitespace() throws TomlParsingException{
        if (isAtFileEnd) {// getting a character here throws an error
            return 0;
        }
        int whitespaceAmount = getTrailingWhitespace();

        advance(whitespaceAmount);

        return whitespaceAmount;
    }

    private int getTrailingWhitespace() {
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
            if (TomlStringUtils.isWhitespace(chars.codePointAt(i)) && chars.codePointAt(i) != '\n') {
                whitespace += 1;
            } else {
                break;
            }
        }
        return whitespace;
    }

    public int getIndex() {
        return index;
    }

    public int current() throws TomlParsingException {
        if (isAtFileEnd)
            throw new TomlParsingException("Cannot pull next the end of a the content");
        return chars.codePointAt(index);
    }

    public String getCurrentLine() {
        int endIndex = contentSize-1;
        for (int i = index;i<contentSize;i++) {
            if (chars.codePointAt(i) == '\n') {
                endIndex = i;
                break;
            }
        }
        int startIndex = 0;
        for (int i = index;i >= 0;i--) {
            if (chars.codePointAt(i) == '\n') {
                startIndex = i;
                break;
            }
        }
        return chars.substring(startIndex, endIndex);
    }

    public int next() throws TomlParsingException{

        if (isAtFileEnd)
            throw new TomlParsingException("Cannot pull next the end of a the content");
        return chars.codePointAt(index+1);
    }
    public int next(int next) throws TomlParsingException{

        if (isAtFileEnd)
            throw new TomlParsingException("Cannot pull next the end of a the content");
        return chars.codePointAt(index+next);
    }

    public boolean nextCharactersExist(int amount) {
        if (index < 0)
            return false;
        return index < contentSize - amount;
    }

    public int get(String chars) {
        return chars.codePointAt(index);
    }

    public boolean isAtFileEndOrLineBreak() {
        if (isAtFileEnd) {
            return true;
        }
        return chars.codePointAt(index) == '\n';
    }

}
