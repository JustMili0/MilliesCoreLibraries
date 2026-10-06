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

import java.util.function.Predicate;

public class TagUtil {

    public static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registries, ResourceLocation id) {
        return TagKey.create(registries, id);
    }

    public static Predicate<BlockState> matchTags(BlockState state, String namespace, String endsWith) {
        var tags = state.getTags().filter(tag -> tag.location().getNamespace().equals(namespace)
            && tag.location().getPath().endsWith(endsWith)).toList();
        if (tags.isEmpty()) return s -> s.is(state.getBlock());
        return s -> tags.stream().anyMatch(s::is);
    }

    public static Predicate<BlockState> matchOre(BlockState state) {
        // 1.20.1 - ends with "ores", 1.21.1 - starts with "ores/"
        return matchTags(state, "c", "ores");
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
