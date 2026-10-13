package net.justmili.corelibs.util.client.datagen;

import com.google.gson.JsonElement;
import net.justmili.corelibs.util.client.TextureMap;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.Tilt;

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

    public ResourceLocation getModelId(Block block) {
        return ModelLocationUtils.getModelLocation(block);
    }

    public ResourceLocation getModelId(Block block, String suffix) {
        return ModelLocationUtils.getModelLocation(block, suffix);
    }

    public Variant getPlainVariantId(Block block, String suffix) {
        return Variant.variant().with(VariantProperties.MODEL, getModelId(block, suffix));
    }

    private TexturedModel getParentFrom(Block block) {
        return TexturedModel.CUBE.get(block);
    }

    private TextureMapping getParentMapping(Block block) {
        return getParentFrom(block).getMapping();
    }

    public MultiVariantGenerator multiVariant(Block block) {
        return MultiVariantGenerator.multiVariant(block, Variant.variant());
    }

    public MultiVariantGenerator multiVariant(Block block, Variant variant) {
        return MultiVariantGenerator.multiVariant(block, variant);
    }

    public MultiVariantGenerator multiVariant(Block block, Variant... variants) {
        return MultiVariantGenerator.multiVariant(block, variants);
    }

    public MultiVariantGenerator simpleBlock(Block block, ResourceLocation modelId) {
        return BlockModelGenerators.createSimpleBlock(block, modelId);
    }

    private void nonTemplateModel(Block block, Block modelBlock) {
        gen.createNonTemplateModelBlock(block, modelBlock);
    }

    private void nonTemplateModel(Block block) {
        gen.createNonTemplateModelBlock(block);
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

    public void cube(Block block, ResourceLocation up, ResourceLocation down, ResourceLocation north, ResourceLocation south, ResourceLocation west, ResourceLocation east, ResourceLocation particle) {
        gen.createTrivialBlock(block, new TextureMapping()
                .put(TextureSlot.DOWN, down)
                .put(TextureSlot.UP, up)
                .put(TextureSlot.NORTH, north)
                .put(TextureSlot.SOUTH, south)
                .put(TextureSlot.EAST, east)
                .put(TextureSlot.WEST, west)
                .put(TextureSlot.PARTICLE, particle),
            ModelTemplates.CUBE
        );
    }

    public void cube(Block block, ResourceLocation up, ResourceLocation down, ResourceLocation north, ResourceLocation south, ResourceLocation west, ResourceLocation east) {
        cube(block, up, down, north, south, west, east, TextureMap.side(block));
    }

    public void doubleBlock(Block block, ResourceLocation topHalfModelId, ResourceLocation bottomHalfModelId) {
        gen.createDoubleBlock(block, topHalfModelId, bottomHalfModelId);
    }

    public enum PillarRotation {
        NONE, // No rotation, just top, bottom and side textures
        LOG_XYZ, // X/Y/Z rotation, one model rotated to fit the axis. Works for distinct end textures and for all 6 sides being the same texture
        LOG_XYZ_HORIZONTAL, // Same as LOG_XYZ but with a separate model for side placement
        LOG_XYZ_UV_LOCKED, // Same as LOG_XYZ but with a UV locked model per axis
        LOG_XYZ_MIRRORED, // Same as LOG_XYZ but with randomly mirrored and rotated variants
        ALL_DIRECTIONS // D/U/N/S/W/E facing, column shaped
    }

    public void pillar(Block block, TextureMapping mapping, PillarRotation rotation) {
        switch (rotation) {
            case NONE -> gen.createTrivialBlock(block, TexturedModel.COLUMN);
            case LOG_XYZ -> create(BlockModelGenerators.createAxisAlignedPillarBlock(block,
                ModelTemplates.CUBE_COLUMN.create(block, mapping, modelOutput)));
            case LOG_XYZ_HORIZONTAL -> create(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(block,
                ModelTemplates.CUBE_COLUMN.create(block, mapping, modelOutput),
                ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(block, mapping, modelOutput)));
            case LOG_XYZ_UV_LOCKED -> create(BlockModelGenerators.createPillarBlockUVLocked(block, mapping, modelOutput));
            case LOG_XYZ_MIRRORED -> create(BlockModelGenerators.createMirroredColumnGenerator(block,
                ModelTemplates.CUBE_COLUMN.create(block, mapping, modelOutput), mapping, modelOutput));
            case ALL_DIRECTIONS -> create(multiVariant(block, Variant.variant().with(VariantProperties.MODEL,
                ModelTemplates.CUBE_COLUMN.create(block, mapping, modelOutput))).with(gen.createColumnWithFacing()));
        }
    }

    public void log(Block block) {
        pillar(block, TextureMapping.logColumn(block), PillarRotation.LOG_XYZ_HORIZONTAL);
    }

    public void wood(Block block) {
        var logMapping = TextureMapping.logColumn(block);
        pillar(block, logMapping.copyAndUpdate(TextureSlot.END, logMapping.get(TextureSlot.SIDE)), PillarRotation.LOG_XYZ);
    }

    public void stairs(Block parent, Block block) {
        var mapping = getParentMapping(parent);
        var straight = ModelTemplates.STAIRS_STRAIGHT.create(block, mapping, modelOutput);
        var inner = ModelTemplates.STAIRS_INNER.create(block, mapping, modelOutput);
        var outer = ModelTemplates.STAIRS_OUTER.create(block, mapping, modelOutput);

        create(BlockModelGenerators.createStairs(block, inner, straight, outer));
        create(block, straight);
    }

    public void slab(Block parent, Block block) {
        var mapping = getParentMapping(parent);
        var bottom = ModelTemplates.SLAB_BOTTOM.create(block, mapping, modelOutput);
        var top = ModelTemplates.SLAB_TOP.create(block, mapping, modelOutput);
        var full = getParentFrom(parent).create(parent, modelOutput);

        create(BlockModelGenerators.createSlab(block, bottom, top, full));
        create(block, bottom);
    }

    public void fence(Block parent, Block block) {
        var mapping = getParentMapping(parent);
        var fencePost = ModelTemplates.FENCE_POST.create(block, mapping, modelOutput);
        var fenceSide = ModelTemplates.FENCE_SIDE.create(block, mapping, modelOutput);

        create(BlockModelGenerators.createFence(block, fencePost, fenceSide));
        create(block, ModelTemplates.FENCE_INVENTORY.create(block, mapping, modelOutput));
    }

    public void fenceGate(Block parent, Block block) {
        var mapping = getParentMapping(parent);
        var open = ModelTemplates.FENCE_GATE_OPEN.create(block, mapping, modelOutput);
        var closed = ModelTemplates.FENCE_GATE_CLOSED.create(block, mapping, modelOutput);
        var wallOpen = ModelTemplates.FENCE_GATE_WALL_OPEN.create(block, mapping, modelOutput);
        var wallClosed = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(block, mapping, modelOutput);

        create(BlockModelGenerators.createFenceGate(block, open, closed, wallOpen, wallClosed, true));
        // No item model for some reason
    }

    public void wall(Block parent, Block block) {
        var mapping = getParentMapping(parent);
        var wallPost = ModelTemplates.WALL_POST.create(block, mapping, modelOutput);
        var wallLow = ModelTemplates.WALL_LOW_SIDE.create(block, mapping, modelOutput);
        var wallTall = ModelTemplates.WALL_TALL_SIDE.create(block, mapping, modelOutput);

        create(BlockModelGenerators.createWall(block, wallPost, wallLow, wallTall));
        create(block, ModelTemplates.WALL_INVENTORY.create(block, mapping, modelOutput));
    }

    public void door(Block block) {
        gen.createDoor(block);
    }

    public void directionalTrapdoor(Block block) {
        gen.createOrientableTrapdoor(block);
    }

    public void trapdoor(Block block) {
        gen.createTrapdoor(block);
    }

    public void button(Block parent, Block block) {
        var mapping = getParentMapping(parent);
        var button = ModelTemplates.BUTTON.create(block, mapping, modelOutput);
        var pressed = ModelTemplates.BUTTON_PRESSED.create(block, mapping, modelOutput);

        create(BlockModelGenerators.createButton(block, button, pressed));
        create(block, ModelTemplates.BUTTON_INVENTORY.create(block, mapping, modelOutput));
    }

    public void pressurePlate(Block parent, Block block) {
        var mapping = getParentMapping(parent);
        var plate = ModelTemplates.PRESSURE_PLATE_UP.create(block, mapping, modelOutput);
        var pressed = ModelTemplates.PRESSURE_PLATE_DOWN.create(block, mapping, modelOutput);

        create(BlockModelGenerators.createPressurePlate(block, plate, pressed));
        // No item model for some reason
    }

    public void weightedPressurePlate() {
    }

    public void createWoodFamily(Block log, Block wood, Block planks, Block stairs, Block slab, Block fence, Block fenceGate, Block door, Block trapdoor, boolean trapdoorHasDirectionalTexture) {
        log(log);
        wood(wood);
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
        // Weighted pressure plate should be separate
    }

    public void glassBlock(Block block) {
    }

    public void glassPane(Block block) {
    }

    public void createGlassFamily(Block glass, Block pane) {
        glassBlock(glass);
        glassPane(pane);
    }

    public void brushableBlock(Block block) {
        gen.createBrushableBlock(block);
    }

    public void grassBlock(Block block, Block dirt) {
        var mapping = new TextureMapping().put(TextureSlot.BOTTOM, TextureMap.getTexture(dirt)).copyForced(TextureSlot.BOTTOM, TextureSlot.PARTICLE)
            .put(TextureSlot.TOP, TextureMap.getTexture(block, "_top"))
            .put(TextureSlot.SIDE, TextureMap.getTexture(block, "_snow"));
        var variant = Variant.variant().with(VariantProperties.MODEL, ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, "_snow", mapping, modelOutput));
        gen.createGrassLikeBlock(block, getModelId(block), variant);
    }

    public void dirtPath() {
    }

    public void shortGrass() {
    }

    public void tallGrass() {
    }

    public void shortSeaGrass() {
    }

    public void tallSeaGrass() {
    }

    public void plant(Block block, BlockModelGenerators.TintState tint) {
        gen.createCrossBlockWithDefaultItem(block, tint);
    }

    public void plant(Block block, BlockModelGenerators.TintState tint, TextureMapping mapping) {
        gen.createCrossBlockWithDefaultItem(block, tint, mapping);
    }

    public void pottedPlant(Block block, Block potted, BlockModelGenerators.TintState tint) {
        gen.createPlant(block, potted, tint);
    }

    // Useful for foliage
    public void crossBlock(Block block, BlockModelGenerators.TintState tint, Property<Integer> property, int... propertyValues) {
        gen.createCrossBlock(block, tint, property, propertyValues);
    }

    public void crossBlock(Block block, BlockModelGenerators.TintState tint, TextureMapping mapping) {
        gen.createCrossBlock(block, tint, mapping);
    }

    public void crossBlock(Block block, BlockModelGenerators.TintState tint) {
        gen.createCrossBlock(block, tint);
    }

    public void flowerBed() {
    }

    public void bigDripleaf(Block block) {
        gen.skipAutoItemBlock(block);
        var dripleaf = getModelId(block);
        var partial = getModelId(block, "_partial_tilt");
        var full = getModelId(block, "_full_tilt");
        create(multiVariant(block).with(BlockModelGenerators.createHorizontalFacingDispatch()).with(PropertyDispatch.property(BlockStateProperties.TILT)
            .select(Tilt.NONE, Variant.variant().with(VariantProperties.MODEL, dripleaf))
            .select(Tilt.UNSTABLE, Variant.variant().with(VariantProperties.MODEL, dripleaf))
            .select(Tilt.PARTIAL, Variant.variant().with(VariantProperties.MODEL, partial))
            .select(Tilt.FULL, Variant.variant().with(VariantProperties.MODEL, full))));
    }

    public void smallDripleaf() {
    }

    public void stem() {
    }

    public void coralFan() {
    }

    public void createCoralFamily() {
    }
}