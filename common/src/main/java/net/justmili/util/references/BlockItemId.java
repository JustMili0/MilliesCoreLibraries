package net.justmili.util.references;

import net.justmili.util.utils.common.ResourceUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public record BlockItemId(ResourceKey<Block> block, ResourceKey<Item> item) {
    // Copy of Minecraft 26.2 class BlockItemId for earlier Minecraft versions
    public static BlockItemId create(final ResourceLocation blockId, final ResourceLocation itemId) {
        return new BlockItemId(ResourceKey.create(Registries.BLOCK, blockId), ResourceKey.create(Registries.ITEM, itemId));
    }

    public static BlockItemId create(final String blockName, final String itemName) {
        return create(ResourceUtil.asMinecraft(blockName), ResourceUtil.asMinecraft(itemName));
    }

    public static BlockItemId create(final String name) {
        var id = ResourceUtil.asMinecraft(name);
        return create(id, id);
    }
}
