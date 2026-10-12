package net.justmili.corelibs.util.common.datagen;

import net.justmili.corelibs.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagDatagen {

    @SafeVarargs
    private void add(ResourceKey<Item> id, TagKey<Item>... tags) {
        for (var tag : tags) this.tag(tag).add(id);
    }

    @SafeVarargs
    private void add(BlockItemId id, TagKey<Item>... tags) {
        for (var tag : tags) {
            this.tag(tag).add(id.item());
        }
    }

    @SafeVarargs
    private void add(ResourceKey<Block> id, TagKey<Block>... tags) {
        for (var tag : tags) this.tag(tag).add(id);
    }

    @SafeVarargs
    private void add(BlockItemId id, TagKey<Block>... tags) {
        for (var tag : tags) {
            this.tag(tag).add(id.block());
        }
    }
}
