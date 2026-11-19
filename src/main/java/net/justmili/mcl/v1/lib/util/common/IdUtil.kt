package net.justmili.mcl.v1.lib.util.common

import net.minecraft.resources.ResourceLocation

object IdUtil {

    @JvmStatic fun parse(modId: String, path: String) = ResourceLocation(modId, path)
    @JvmStatic fun parse(path: String): ResourceLocation = ResourceLocation(path)
    @JvmStatic fun asMinecraft(path: String) = parse("minecraft", path)
    @JvmStatic fun asCommon(path: String) = parse("c", path)
    @JvmStatic fun asFabric(path: String) = parse("fabric", path)
    @JvmStatic fun asQuilt(path: String) = parse("quilt", path)
    @JvmStatic fun asForge(path: String) = parse("forge", path)
    @JvmStatic fun asNeoForge(path: String) = parse("neoforge", path)
}