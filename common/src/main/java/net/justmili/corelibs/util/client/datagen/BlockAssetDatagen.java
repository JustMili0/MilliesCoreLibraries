package net.justmili.corelibs.util.client.datagen;

import com.google.gson.JsonElement;
import net.justmili.corelibs.util.client.TextureMap;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockAssetDatagen {
    public record BlockRot(VariantProperty<VariantProperties.Rotation> axis, VariantProperties.Rotation rot) {
    }

    public static final BlockRot Y_R90 = new BlockRot(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
    public static final BlockRot Y_R180 = new BlockRot(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
    public static final BlockRot Y_R270 = new BlockRot(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
    public static final BlockRot X_R90 = new BlockRot(VariantProperties.X_ROT, VariantProperties.Rotation.R90);
    public static final BlockRot X_R180 = new BlockRot(VariantProperties.X_ROT, VariantProperties.Rotation.R180);
    public static final BlockRot X_R270 = new BlockRot(VariantProperties.X_ROT, VariantProperties.Rotation.R270);

    private final BlockModelGenerators gen;
    private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
    private final Consumer<BlockStateGenerator> stateOutput;

    public BlockAssetDatagen(BlockModelGenerators modelGenerators) {
        this.gen = modelGenerators;
        this.modelOutput = gen.modelOutput;
        this.stateOutput = gen.blockStateOutput;
    }

    /* TODO:
    - Redo the entire util, code every model generation + make non-standard models generatable
    Remember to make it easy to port to newer versions later! Use lots of helpers!
     */

    public void create(BlockStateGenerator generator) {
        stateOutput.accept(generator);
    }

    public void create(Block block, ResourceLocation template) {
        gen.delegateItemModel(block, template);
    }

    private ResourceLocation getModelId(Block block) {
        return ModelLocationUtils.getModelLocation(block);
    }

    private TexturedModel getParentFrom(Block block) {
        return TexturedModel.CUBE.get(block);
    }

    private TextureMapping getParentMapping(Block block) {
        return getParentFrom(block).getMapping();
    }

    public MultiVariantGenerator simpleBlock(Block block, ResourceLocation modelId) {
        return BlockModelGenerators.createSimpleBlock(block, modelId);
    }

    private void nonTemplateModel(Block block, Block modelBlock) {
        create(simpleBlock(block, getModelId(modelBlock)));
    }

    private void nonTemplateModel(Block block) {
        nonTemplateModel(block, block);
    }

    public void cubeAll(Block block) {
        gen.createTrivialCube(block);
    }

    public void cubeAll(Block block, ResourceLocation texture) {
        gen.createTrivialBlock(block, TextureMapping.cube(texture), ModelTemplates.CUBE_ALL);
    }

    public void cube(Block block) {
        gen.createGenericCube(block);
    }

    public void cube(Block block, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west, ResourceLocation particle) {
        var mapping = new TextureMapping().put(TextureSlot.DOWN, down).put(TextureSlot.UP, up).put(TextureSlot.NORTH, north).put(TextureSlot.SOUTH, south).put(TextureSlot.EAST, east).put(TextureSlot.WEST, west).put(TextureSlot.PARTICLE, particle);
        gen.createTrivialBlock(block, mapping, ModelTemplates.CUBE);
    }

    public void cube(Block block, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west) {
        cube(block, down, up, north, south, east, west, TextureMap.side(block));
    }

    public enum ColumnVariants {
        // TODO: add in all the different pillar and column variants
    }

    public void pillar(Block block, TextureMapping mapping, ColumnVariants type) {
        // TODO: Based on ColumnVariants generate different pillar and column types
        // Also add specific functions that'll use this for certain commonly added blocks such as logs, woods etc
    }

    public void log() {
    }

    public void wood() {
    }

    public void stairs(Block parent, Block stairs) {
        var mapping = getParentMapping(parent);
        var straight = ModelTemplates.STAIRS_STRAIGHT.create(stairs, mapping, modelOutput);
        var inner = ModelTemplates.STAIRS_INNER.create(stairs, mapping, modelOutput);
        var outer = ModelTemplates.STAIRS_OUTER.create(stairs, mapping, modelOutput);

        create(BlockModelGenerators.createStairs(stairs, inner, straight, outer));
        create(stairs, straight);
    }

    public void slab(Block parent, Block slab) {
        var mapping = getParentMapping(parent);
        var bottom = ModelTemplates.SLAB_BOTTOM.create(slab, mapping, modelOutput);
        var top = ModelTemplates.SLAB_TOP.create(slab, mapping, modelOutput);
        var full = getParentFrom(parent).create(parent, modelOutput);

        create(BlockModelGenerators.createSlab(slab, bottom, top, full));
        create(slab, bottom);
    }

    public void fence(Block parent, Block fence) {
        var mapping = getParentMapping(parent);
        var fencePost = ModelTemplates.FENCE_POST.create(fence, mapping, modelOutput);
        var fenceSide = ModelTemplates.FENCE_SIDE.create(fence, mapping, modelOutput);

        create(BlockModelGenerators.createFence(fence, fencePost, fenceSide));
        create(fence, ModelTemplates.FENCE_INVENTORY.create(fence, mapping, modelOutput));
    }

    public void fenceGate(Block parent, Block fenceGate) {
        var mapping = getParentMapping(parent);
        var open = ModelTemplates.FENCE_GATE_OPEN.create(fenceGate, mapping, modelOutput);
        var closed = ModelTemplates.FENCE_GATE_CLOSED.create(fenceGate, mapping, modelOutput);
        var wallOpen = ModelTemplates.FENCE_GATE_WALL_OPEN.create(fenceGate, mapping, modelOutput);
        var wallClosed = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(fenceGate, mapping, modelOutput);

        create(BlockModelGenerators.createFenceGate(fenceGate, open, closed, wallOpen, wallClosed, true));
        // No item model for some reason
    }

    public void wall(Block parent, Block wall) {
        var mapping = getParentMapping(parent);
        var wallPost = ModelTemplates.WALL_POST.create(wall, mapping, modelOutput);
        var wallLow = ModelTemplates.WALL_LOW_SIDE.create(wall, mapping, modelOutput);
        var wallTall = ModelTemplates.WALL_TALL_SIDE.create(wall, mapping, modelOutput);

        create(BlockModelGenerators.createWall(wall, wallPost, wallLow, wallTall));
        create(wall, ModelTemplates.WALL_INVENTORY.create(wall, mapping, modelOutput));
    }

    public void door(Block door) {
        gen.createDoor(door);
    }

    public void directionalTrapdoor(Block trapdoor) {
        gen.createOrientableTrapdoor(trapdoor);
    }

    public void trapdoor(Block trapdoor) {
        gen.createTrapdoor(trapdoor);
    }

    public void button(Block parent, Block button) {
        var mapping = getParentMapping(parent);
        var btn = ModelTemplates.BUTTON.create(button, mapping, modelOutput);
        var pressed = ModelTemplates.BUTTON_PRESSED.create(button, mapping, modelOutput);

        create(BlockModelGenerators.createButton(button, btn, pressed));
        create(button, ModelTemplates.BUTTON_INVENTORY.create(button, mapping, modelOutput));
    }

    public void pressurePlate(Block parent, Block pressurePlate) {
        var mapping = getParentMapping(parent);
        var plate = ModelTemplates.PRESSURE_PLATE_UP.create(pressurePlate, mapping, modelOutput);
        var pressed = ModelTemplates.PRESSURE_PLATE_DOWN.create(pressurePlate, mapping, modelOutput);

        create(BlockModelGenerators.createPressurePlate(pressurePlate, plate, pressed));
        // No item model for some reason
    }

    public void createWoodFamily(Block log, Block wood, Block planks, Block stairs, Block slab, Block fence, Block fenceGate, Block door, Block trapdoor, boolean trapdoorHasDirectionalTexture) {
        // Log
        // Wood
        cubeAll(planks);
        stairs(planks, stairs);
        slab(planks, slab);
        fence(planks, fence);
        fenceGate(planks, fenceGate);
        door(door);
        if (trapdoorHasDirectionalTexture) {
            directionalTrapdoor(trapdoor);
        } else {
            trapdoor(trapdoor);
        }
    }

    public void createStoneFamily(Block stone, Block stairs, Block slab) {
        cubeAll(stone);
        stairs(stone, stairs);
        slab(stone, slab);
    }

    public void createStoneFamily(Block stone, Block stairs, Block slab, Block wall) {
        createStoneFamily(stone, stairs, slab);
        wall(stone, wall);
    }

    public void createPressableFamily(Block parent, Block button, Block pressurePlate) {
        button(parent, button);
        pressurePlate(parent, pressurePlate);
    }

    public void createGlassFamily(Block glass, Block pane) {
        gen.createGlassBlocks(glass, pane);
    }
}
