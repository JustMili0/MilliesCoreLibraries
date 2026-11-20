package net.justmili.mcl.v1.lib.util.common

import net.minecraft.resources.ResourceLocation

object IdUtil {

    @JvmStatic fun parse(modId: String, path: String) = /*? if >= 1.21.1 {*//*ResourceLocation.fromNamespaceAndPath*//*?} else {*/ResourceLocation/*?}*/(modId, path)
    @JvmStatic fun parse(path: String): ResourceLocation = /*? if >= 1.21.1 {*//*ResourceLocation.parse*//*?} else {*/ResourceLocation/*?}*/(path)
    @JvmStatic fun asMinecraft(path: String) = parse("minecraft", path)
    @JvmStatic fun asCommon(path: String) = parse("c", path)
    @JvmStatic fun asFabric(path: String) = parse("fabric", path)
    @JvmStatic fun asQuilt(path: String) = parse("quilt", path)
    @JvmStatic fun asForge(path: String) = parse("forge", path)
    @JvmStatic fun asNeoForge(path: String) = parse("neoforge", path)
}