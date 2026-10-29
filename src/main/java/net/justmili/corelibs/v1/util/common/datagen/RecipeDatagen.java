package net.justmili.corelibs.v1.util.common.datagen;

import net.justmili.corelibs.v1.util.common.ResourceUtil;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class RecipeDatagen {
    private final Consumer<FinishedRecipe> writer;
    private final String modId;

    public RecipeDatagen(String modId, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        this.modId = modId;
        this.writer = finishedRecipeConsumer;
    }

    public static InventoryChangeTrigger.TriggerInstance has(ItemLike itemLike) {
        return new InventoryChangeTrigger.TriggerInstance(
            ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,
            new ItemPredicate[]{ItemPredicate.Builder.item().of(itemLike).build()}
        );
    }

    public static String hasItem(Item item) {
        return "has_" + getItemName(item);
    }

    private static String getItemName(Item item) {
        return RecipeProvider.getItemName(item);
    }

    private ResourceLocation output(Item item) {
        return ResourceUtil.parse(modId, getItemName(item));
    }

    private ResourceLocation outputFrom(Item input, Item output) {
        return ResourceUtil.parse(modId, getItemName(output) + "_from_" + getItemName(input));
    }

    private ResourceLocation outputFromProcess(Item input, Item output, String processType) {
        return ResourceUtil.parse(modId, getItemName(output) + "_from_" + getItemName(input) + processType);
    }

    public void shapeless(RecipeCategory category, Item output, int outCount, Item... inputs) {
        var builder = ShapelessRecipeBuilder.shapeless(category, output, outCount);
        for (var input : inputs) {
            builder.requires(input);
        }
        var inputNames = Arrays.stream(inputs).map(RecipeProvider::getItemName).collect(Collectors.joining("_and_"));
        builder.unlockedBy(hasItem(inputs[0]), has(inputs[0])).save(writer, ResourceUtil.parse(modId, getItemName(output) + "_from_" + inputNames));
    }

    public void shapeless(RecipeCategory category, Item output, Item... inputs) {
        shapeless(category, output, 1, inputs);
    }

    public void shaped2x2(RecipeCategory category, Item material, Item output, int outCount) {
        ShapedRecipeBuilder.shaped(category, output, outCount)
            .define('#', material)
            .pattern("##")
            .pattern("##")
            .unlockedBy(hasItem(material), has(material))
            .save(writer, ResourceUtil.parse(modId, getItemName(output)));
    }

    public void shaped3x3(RecipeCategory category, Item material, Item output, int outCount) {
        ShapedRecipeBuilder.shaped(category, output, outCount)
            .define('#', material)
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .unlockedBy(hasItem(material), has(material))
            .save(writer, ResourceUtil.parse(modId, getItemName(output)));
    }

    public void planks(Item log, Item planks) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
            .requires(log)
            .unlockedBy(hasItem(log), has(log))
            .save(writer, ResourceUtil.parse(modId, getItemName(planks)));
    }

    public void planksFromLogs(Item log, Item planks) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
            .requires(log)
            .unlockedBy(hasItem(log), has(log))
            .save(writer, outputFrom(log, planks));
    }

    public void planksFromWood(Item wood, Item planks) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
            .requires(wood)
            .unlockedBy(hasItem(wood), has(wood))
            .save(writer, outputFrom(wood, planks));
    }

    public void stairs(Item material, Item stairs) {
        RecipeProvider.stairBuilder(stairs, Ingredient.of(material))
            .unlockedBy(hasItem(material), has(material))
            .save(writer, output(stairs));
    }

    public void slab(Item material, Item slab) {
        RecipeProvider.slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, Ingredient.of(material))
            .unlockedBy(hasItem(material), has(material))
            .save(writer, output(slab));
    }

    public void fence(Item material, Item fence) {
        RecipeProvider.fenceBuilder(fence, Ingredient.of(material))
            .unlockedBy(hasItem(material), has(material))
            .save(writer, output(fence));
    }

    public void fenceGate(Item material, Item fenceGate) {
        RecipeProvider.fenceGateBuilder(fenceGate, Ingredient.of(material))
            .unlockedBy(hasItem(material), has(material))
            .save(writer, output(fenceGate));
    }

    public void door(Item material, Item door) {
        RecipeProvider.doorBuilder(door, Ingredient.of(material))
            .unlockedBy(hasItem(material), has(material))
            .save(writer, output(door));
    }

    public void trapdoor(Item material, Item trapdoor) {
        RecipeProvider.trapdoorBuilder(trapdoor, Ingredient.of(material))
            .unlockedBy(hasItem(material), has(material))
            .save(writer, output(trapdoor));
    }

    public void wall(Item material, Item wall) {
        RecipeProvider.wallBuilder(RecipeCategory.BUILDING_BLOCKS, wall, Ingredient.of(material))
            .unlockedBy(hasItem(material), has(material))
            .save(writer, output(wall));
    }

    public void bars(Item material, Item bars) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, bars, 16)
            .define('#', material)
            .pattern("###")
            .pattern("###")
            .unlockedBy(hasItem(material), has(material))
            .save(writer, output(bars));
    }

    public void pressurePlate(Item material, Item pressurePlate) {
        RecipeProvider.pressurePlateBuilder(RecipeCategory.REDSTONE, pressurePlate, Ingredient.of(material))
            .unlockedBy(hasItem(material), has(material))
            .save(writer, output(pressurePlate));
    }

    public void button(Item material, Item button) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button)
            .requires(material)
            .unlockedBy(hasItem(material), has(material))
            .save(writer, output(button));
    }

    public void carpet(Item material, Item carpet) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, carpet, 3)
            .define('#', material)
            .pattern("##")
            .unlockedBy(hasItem(material), has(material))
            .save(writer, output(carpet));
    }

    public void dyeItem(Item item, Item dye, Item dyed) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, dyed)
            .requires(item).requires(dye)
            .unlockedBy(hasItem(dye), has(dye))
            .save(writer, ResourceUtil.parse(modId, getItemName(dyed) + "_from_dying"));
    }

    public void dyeItem(TagKey<Item> items, Item dye, Item dyed) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, dyed)
            .requires(items).requires(dye)
            .unlockedBy(hasItem(dye), has(dye))
            .save(writer, ResourceUtil.parse(modId, getItemName(dyed) + "_from_dying"));
    }

    public void smelt(Item input, Item output, float exp, int cookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
            .unlockedBy(hasItem(input), has(input))
            .save(writer, outputFromProcess(input, output, "_smelting"));
    }

    public void smelt(Item input, Item output, float exp) {
        smelt(input, output, exp, 200);
    }

    public void blast(Item input, Item output, float exp, int cookingTime) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
            .unlockedBy(hasItem(input), has(input))
            .save(writer, outputFromProcess(input, output, "_blasting"));
    }

    public void blast(Item input, Item output, float exp) {
        blast(input, output, exp, 100);
    }

    public void smoke(Item input, Item output, float exp, int cookingTime) {
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), RecipeCategory.MISC, output, exp, cookingTime)
            .unlockedBy(hasItem(input), has(input))
            .save(writer, outputFromProcess(input, output, "_smoking"));
    }

    public void smoke(Item input, Item output, float exp) {
        smoke(input, output, exp, 100);
    }

    public void cut(Item input, Item output, int resultCount) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output, resultCount)
            .unlockedBy(hasItem(input), has(input))
            .save(writer, outputFromProcess(input, output, "_stonecutting"));
    }

    public void cut(Item input, Item output) {
        cut(input, output, 1);
    }

    public void smithing(Item base, Item addition, Item template, RecipeCategory category, Item result) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(base), Ingredient.of(addition), category, result)
            .unlocks(hasItem(addition), has(addition))
            .save(writer, outputFromProcess(result, base, "_smithing"));
    }
}