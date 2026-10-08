package net.justmili.util.utils.common;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.function.Predicate;

public class TagUtil {

    public static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registries, ResourceLocation id) {
        return TagKey.create(registries, id);
    }

    public static Predicate<BlockState> matchBlockTags(BlockState state, String namespace, String containsPath) {
        var matchingTags = new ArrayList<TagKey<Block>>();
        var tagIterator = state.getTags().iterator();
        while (tagIterator.hasNext()) {
            var tag = tagIterator.next();
            var tagId = tag.location();
            if (tagId.getNamespace().equals(namespace) && tagId.getPath().endsWith(containsPath)) matchingTags.add(tag);
        }

        if (matchingTags.isEmpty()) return s -> s.is(state.getBlock());
        return s -> {
            for (var tag : matchingTags) {
                if (s.is(tag)) return true;
            }
            return false;
        };
    }

    public static Predicate<BlockState> matchOreTags(BlockState state) {
        // 1.20.1 - ends with "ores", 1.21.1 - starts with "ores/"
        return matchBlockTags(state, "c", "ores");
    }

    public static TagKey<Block> block(ResourceLocation id) {
        return create(Registries.BLOCK, id);
    }

    public static TagKey<Item> item(ResourceLocation id) {
        return create(Registries.ITEM, id);
    }

    public static TagKey<Biome> biome(ResourceLocation id) {
        return create(Registries.BIOME, id);
    }

    public static TagKey<EntityType<?>> entityType(ResourceLocation id) {
        return create(Registries.ENTITY_TYPE, id);
    }

    public static TagKey<Enchantment> enchant(ResourceLocation id) {
        return create(Registries.ENCHANTMENT, id);
    }
}