package net.justmili.corelibs.libs.util.client.datagen;

import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemAssetDatagen {
    private final ItemModelGenerators gen;

    public ItemAssetDatagen(ItemModelGenerators modelGenerators) {
        this.gen = modelGenerators;
    }

    public void flatItem(Item item) {
        gen.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    public void flatBlockItem(Block block) {
        gen.generateFlatItem(Item.BY_BLOCK.get(block), ModelTemplates.FLAT_ITEM);
    }
}
