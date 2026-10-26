package net.justmili.corelibs.v2.config.flags;

public enum FileTypes {
    TEXT("txt"), // CORELIBS: .txt configs would pretty much look like eula.txt
    JSON("json"),
    JSON5("json5"),
    PROPERTIES("properties"),
    TOML("toml");

    private final String extension;

    FileTypes(String ext) {
        this.extension = ext;
    }

    public String extension() {
        return extension;
    }
}