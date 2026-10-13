package net.justmili.corelibs.util.client;

import net.justmili.corelibs.util.common.ResourceUtil;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Textures {
    public static final String TOP_SUFFIX = "_top";
    public static final String DOWN_SUFFIX = "_bottom";
    public static final String FRONT_SUFFIX = "_front";
    public static final String LEFT_SUFFIX = "_left";
    public static final String RIGHT_SUFFIX = "_right";
    public static final String SIDE_SUFFIX = "_side";
    public static final String SIDE1_SUFFIX = "_side1";
    public static final String SIDE2_SUFFIX = "_side2";
    public static final String SIDE3_SUFFIX = "_side3";
    public static final String SNOW_SUFFIX = "_snow";
    public static final String LIT_SUFFIX = "_on";
    public static final String MOIST_SUFFIX = "_moist";
    public static final String FRONT_RIGHT_SUFFIX = "_front_right";
    public static final String FRONT_LEFT_SUFFIX = "_front_left";
    public static final String BACK_RIGHT_SUFFIX = "_back_right";
    public static final String BACK_LEFT_SUFFIX = "_back_left";
    public static final String FRONT_ON_SUFFIX = "_front_on";
    public static final String PARTIAL_TILT_SUFFIX = "_partial_tilt";
    public static final String FULL_TILT_SUFFIX = "_full_tilt";
    public static final String STAGE0_SUFFIX = "_stage0";
    public static final String STAGE1_SUFFIX = "_stage1";
    public static final String STAGE2_SUFFIX = "_stage2";
    public static final String TOP_STAGE_AFFIX = "_top_stage_";
    public static final String DOWN_STAGE_AFFIX = "_bottom_stage_";

    public static ResourceLocation get(Block block) {
        return TextureMapping.getBlockTexture(block);
    }

    public static ResourceLocation get(Block block, String textureSuffix) {
        return TextureMapping.getBlockTexture(block, textureSuffix);
    }

    public static ResourceLocation get(Item item) {
        return TextureMapping.getItemTexture(item);
    }

    public static ResourceLocation get(Item item, String textureSuffix) {
        return TextureMapping.getItemTexture(item, textureSuffix);
    }

    public static ResourceLocation getBlockPath(String modId, String path) {
        return ResourceUtil.parse(modId, "block/" + path);
    }

    public static ResourceLocation getItemPath(String modId, String path) {
        return ResourceUtil.parse(modId, "item/" + path);
    }

    // Blocks
    public static ResourceLocation top(Block block) {
        return get(block, TOP_SUFFIX);
    }

    public static ResourceLocation down(Block block) {
        return get(block, DOWN_SUFFIX);
    }

    public static ResourceLocation front(Block block) {
        return get(block, FRONT_SUFFIX);
    }

    public static ResourceLocation left(Block block) {
        return get(block, LEFT_SUFFIX);
    }

    public static ResourceLocation right(Block block) {
        return get(block, RIGHT_SUFFIX);
    }

    public static ResourceLocation side(Block block) {
        return get(block, SIDE_SUFFIX);
    }

    // Block variants
    public static ResourceLocation snow(Block block) {
        return get(block, SNOW_SUFFIX);
    }

    public static ResourceLocation lit(Block block) {
        return get(block, LIT_SUFFIX);
    }

    public static ResourceLocation moist(Block block) {
        return get(block, MOIST_SUFFIX);
    }

    // Chest texture suffixes
    public static ResourceLocation frontRight(Block block) {
        return get(block, FRONT_RIGHT_SUFFIX);
    }

    public static ResourceLocation frontLeft(Block block) {
        return get(block, FRONT_LEFT_SUFFIX);
    }

    public static ResourceLocation backRight(Block block) {
        return get(block, BACK_RIGHT_SUFFIX);
    }

    public static ResourceLocation backLeft(Block block) {
        return get(block, BACK_LEFT_SUFFIX);
    }

    public static ResourceLocation frontOn(Block block) {
        return get(block, FRONT_ON_SUFFIX);
    }
}