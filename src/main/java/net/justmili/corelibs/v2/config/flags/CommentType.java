package net.justmili.corelibs.v2.config.flags;

public enum CommentType {
    NONE(""),
    SLASH("// "),
    POUND("# ");

    private final String token;

    CommentType(String token) {
        this.token = token;
    }

    public String token() {
        return token;
    }
}
