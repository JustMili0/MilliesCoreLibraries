package net.justmili.corelibs.v1.util.common;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
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

    public static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registries, Identifier id) {
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

        // Same block as the given one always has the tags, so comparing it first skips looking them up
        var block = state.getBlock();
        if (matchingTags.isEmpty()) return s -> s.is(block);
        if (matchingTags.size() == 1) {
            // .get(0); for possible 1.20.1 port (Java 17)
            return s -> s.is(block) || s.is(matchingTags.get(0));
        }

        return s -> {
            if (s.is(block)) return true;
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

    public static TagKey<Block> block(Identifier id) {
        return create(Registries.BLOCK, id);
    }

    public static TagKey<Item> item(Identifier id) {
        return create(Registries.ITEM, id);
    }

    public static TagKey<Biome> biome(Identifier id) {
        return create(Registries.BIOME, id);
    }

    public static TagKey<EntityType<?>> entityType(Identifier id) {
        return create(Registries.ENTITY_TYPE, id);
    }

    public static TagKey<Enchantment> enchant(Identifier id) {
        return create(Registries.ENCHANTMENT, id);
    }
}