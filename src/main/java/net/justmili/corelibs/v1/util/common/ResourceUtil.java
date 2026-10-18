package net.justmili.corelibs.v1.util.common;

import net.minecraft.resources.Identifier;

public class ResourceUtil {
    public static Identifier parse(String modId, String path) {
        return new Identifier(modId, path);
    }

    public static Identifier asPath(String path) {
        return new Identifier(path);
    }

    public static Identifier asMinecraft(String path) {
        return parse("minecraft", path);
    }

    public static Identifier asCommon(String path) {
        return parse("c", path);
    }

    public static Identifier asFabric(String path) {
        return parse("fabric", path);
    }

    public static Identifier asQuilt(String path) {
        return parse("quilt", path);
    }

    public static Identifier asForge(String path) {
        return parse("forge", path);
    }

    public static Identifier asNeoForge(String path) {
        return parse("neoforge", path);
    }
}
