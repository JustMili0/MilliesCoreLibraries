package net.justmili.corelibs.libs.util.common;

import net.minecraft.resources.ResourceLocation;

public class ResourceUtil {
    public static ResourceLocation parse(String modId, String path) {
        return new ResourceLocation(modId, path);
    }

    public static ResourceLocation asPath(String path) {
        return new ResourceLocation(path);
    }

    public static ResourceLocation asMinecraft(String path) {
        return parse("minecraft", path);
    }

    public static ResourceLocation asCommon(String path) {
        return parse("c", path);
    }

    public static ResourceLocation asFabric(String path) {
        return parse("fabric", path);
    }

    public static ResourceLocation asQuilt(String path) {
        return parse("quilt", path);
    }

    public static ResourceLocation asForge(String path) {
        return parse("forge", path);
    }

    public static ResourceLocation asNeoForge(String path) {
        return parse("neoforge", path);
    }
}
