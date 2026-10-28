package net.justmili.corelibs.v2.config;

public enum FileTypes {
    JSON(".json"),
    JSON5(".json5"),
    PROPERTIES(".properties"),
    TOML(".toml");

    private final String extension;

    FileTypes(String extension) {
        this.extension = extension;
    }

    public String extension() {
        return extension;
    }
}