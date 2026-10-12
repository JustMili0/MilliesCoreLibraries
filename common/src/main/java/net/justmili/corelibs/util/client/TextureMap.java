package net.justmili.corelibs.util.client;

import net.justmili.corelibs.util.common.ResourceUtil;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TextureMap {

    // Generic blocks and items
    public static ResourceLocation asBlockPath(String modId, String path) {
        return ResourceUtil.parse(modId, "block/" + path);
    }

    public static ResourceLocation asItemPath(String modId, String path) {
        return ResourceUtil.parse(modId, "item/" + path);
    }

    public static ResourceLocation top(Block block) {
        return getTexture(block, "_top");
    }

    public static ResourceLocation bottom(Block block) {
        return getTexture(block, "_bottom");
    }

    public static ResourceLocation front(Block block) {
        return getTexture(block, "_front");
    }

    public static ResourceLocation left(Block block) {
        return getTexture(block, "_left");
    }

    public static ResourceLocation right(Block block) {
        return getTexture(block, "_right");
    }

    public static ResourceLocation side(Block block) {
        return getTexture(block, "_side");
    }

    // Chest texture suffixes
    public static ResourceLocation frontRight(Block block) {
        return getTexture(block, "_front_right");
    }

    public static ResourceLocation frontLeft(Block block) {
        return getTexture(block, "_front_left");
    }

    public static ResourceLocation backRight(Block block) {
        return getTexture(block, "_back_right");
    }

    public static ResourceLocation backLeft(Block block) {
        return getTexture(block, "_back_left");
    }

    public static ResourceLocation frontOn(Block block) {
        return getTexture(block, "_front_on");
    }

    public static ResourceLocation lit(Block block) {
        return getTexture(block, "_on");
    }

    public static ResourceLocation moist(Block block) {
        return getTexture(block, "_moist");
    }

    public static ResourceLocation getTexture(Block block) {
        return TextureMapping.getBlockTexture(block);
    }

    public static ResourceLocation getTexture(Block block, String textureSuffix) {
        return TextureMapping.getBlockTexture(block, textureSuffix);
    }

    public static ResourceLocation getTexture(Item item) {
        return TextureMapping.getItemTexture(item);
    }

    public static ResourceLocation getTexture(Item item, String textureSuffix) {
        return TextureMapping.getItemTexture(item, textureSuffix);
    }
}
