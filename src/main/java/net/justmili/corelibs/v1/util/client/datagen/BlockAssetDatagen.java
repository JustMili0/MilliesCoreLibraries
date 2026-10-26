package net.justmili.corelibs.v1.util.client.datagen;

import com.google.gson.JsonElement;
import net.justmili.corelibs.v1.util.client.Assets;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PitcherCropBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.Tilt;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

    // Helpers
    public void create(BlockStateGenerator generator) {
        stateOutput.accept(generator);
    }

    public void create(Block block, ResourceLocation template) {
        gen.delegateItemModel(block, template);
    }

    public Variant getPlainVariantId(Block block, String suffix) {
        return Variant.variant().with(VariantProperties.MODEL, Assets.getModelId(block, suffix));
    }

    private TexturedModel getCubeParent(Block block) {
        return TexturedModel.CUBE.get(block);
    }

    private TextureMapping getCubeParentMapping(Block block) {
        return getCubeParent(block).getMapping();
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

    // Utils
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
        cube(block, up, down, north, south, west, east, Assets.front(block));
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
        var mapping = getCubeParentMapping(parent);
        var straight = ModelTemplates.STAIRS_STRAIGHT.create(block, mapping, modelOutput);
        var inner = ModelTemplates.STAIRS_INNER.create(block, mapping, modelOutput);
        var outer = ModelTemplates.STAIRS_OUTER.create(block, mapping, modelOutput);

        create(BlockModelGenerators.createStairs(block, inner, straight, outer));
        create(block, straight);
    }

    public void slab(Block parent, Block block) {
        var mapping = getCubeParentMapping(parent);
        var bottom = ModelTemplates.SLAB_BOTTOM.create(block, mapping, modelOutput);
        var top = ModelTemplates.SLAB_TOP.create(block, mapping, modelOutput);
        var full = getCubeParent(parent).create(parent, modelOutput);

        create(BlockModelGenerators.createSlab(block, bottom, top, full));
        create(block, bottom);
    }

    public void fence(Block parent, Block block) {
        var mapping = getCubeParentMapping(parent);
        var fencePost = ModelTemplates.FENCE_POST.create(block, mapping, modelOutput);
        var fenceSide = ModelTemplates.FENCE_SIDE.create(block, mapping, modelOutput);

        create(BlockModelGenerators.createFence(block, fencePost, fenceSide));
        create(block, ModelTemplates.FENCE_INVENTORY.create(block, mapping, modelOutput));
    }

    public void fenceGate(Block parent, Block block) {
        var mapping = getCubeParentMapping(parent);
        var open = ModelTemplates.FENCE_GATE_OPEN.create(block, mapping, modelOutput);
        var closed = ModelTemplates.FENCE_GATE_CLOSED.create(block, mapping, modelOutput);
        var wallOpen = ModelTemplates.FENCE_GATE_WALL_OPEN.create(block, mapping, modelOutput);
        var wallClosed = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(block, mapping, modelOutput);

        create(BlockModelGenerators.createFenceGate(block, open, closed, wallOpen, wallClosed, true));
        // No item model for some reason
    }

    public void wall(Block parent, Block block) {
        var mapping = getCubeParentMapping(parent);
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
        var mapping = getCubeParentMapping(parent);
        var button = ModelTemplates.BUTTON.create(block, mapping, modelOutput);
        var pressed = ModelTemplates.BUTTON_PRESSED.create(block, mapping, modelOutput);

        create(BlockModelGenerators.createButton(block, button, pressed));
        create(block, ModelTemplates.BUTTON_INVENTORY.create(block, mapping, modelOutput));
    }

    public void pressurePlate(Block parent, Block block) {
        var mapping = getCubeParentMapping(parent);
        var plate = ModelTemplates.PRESSURE_PLATE_UP.create(block, mapping, modelOutput);
        var pressed = ModelTemplates.PRESSURE_PLATE_DOWN.create(block, mapping, modelOutput);

        create(BlockModelGenerators.createPressurePlate(block, plate, pressed));
        // No item model for some reason
    }

    public void weightedPressurePlate(Block parent, Block block) {
        gen.createWeightedPressurePlate(block, parent);
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
        gen.createTrivialBlock(block, TexturedModel.CUBE);
    }

    public void glassPane(Block parent, Block block) {
        var mapping = TextureMapping.pane(parent, block);
        var post = ModelTemplates.STAINED_GLASS_PANE_POST.create(block, mapping, modelOutput);
        var side = ModelTemplates.STAINED_GLASS_PANE_SIDE.create(block, mapping, modelOutput);
        var sideAlt = ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.create(block, mapping, modelOutput);
        var noSide = ModelTemplates.STAINED_GLASS_PANE_NOSIDE.create(block, mapping, modelOutput);
        var noSideAlt = ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.create(block, mapping, modelOutput);
        ModelTemplates.FLAT_ITEM.create(Assets.getModelId(block.asItem()), TextureMapping.layer0(parent), modelOutput);

        create(MultiPartGenerator.multiPart(block).with(Variant.variant().with(VariantProperties.MODEL, post))
            .with(Condition.condition().term(BlockStateProperties.NORTH, true), Variant.variant().with(VariantProperties.MODEL, side))
            .with(Condition.condition().term(BlockStateProperties.EAST, true), Variant.variant().with(VariantProperties.MODEL, side).with(Y_R90.axis, Y_R90.rot))
            .with(Condition.condition().term(BlockStateProperties.SOUTH, true), Variant.variant().with(VariantProperties.MODEL, sideAlt))
            .with(Condition.condition().term(BlockStateProperties.WEST, true), Variant.variant().with(VariantProperties.MODEL, sideAlt).with(Y_R90.axis, Y_R90.rot))
            .with(Condition.condition().term(BlockStateProperties.NORTH, false), Variant.variant().with(VariantProperties.MODEL, noSide))
            .with(Condition.condition().term(BlockStateProperties.EAST, false), Variant.variant().with(VariantProperties.MODEL, noSideAlt))
            .with(Condition.condition().term(BlockStateProperties.SOUTH, false), Variant.variant().with(VariantProperties.MODEL, noSideAlt).with(Y_R90.axis, Y_R90.rot))
            .with(Condition.condition().term(BlockStateProperties.WEST, false), Variant.variant().with(VariantProperties.MODEL, noSide).with(Y_R270.axis, Y_R270.rot))
        );
    }

    public void createGlassFamily(Block glass, Block pane) {
        glassBlock(glass);
        glassPane(glass, pane);
    }

    public void brushableBlock(Block block) {
        gen.createBrushableBlock(block);
    }

    public void grassBlock(Block block, Block dirt) {
        var mapping = new TextureMapping().put(TextureSlot.BOTTOM, Assets.get(dirt)).copyForced(TextureSlot.BOTTOM, TextureSlot.PARTICLE)
            .put(TextureSlot.TOP, Assets.top(block))
            .put(TextureSlot.SIDE, Assets.snow(block));
        var variant = Variant.variant().with(VariantProperties.MODEL, ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block, Assets.SNOW_SUFFIX, mapping, modelOutput));

        gen.createGrassLikeBlock(block, Assets.getModelId(block), variant);
    }

    public void farmland(Block block, Block dirt) {
        var mappingDry = new TextureMapping().put(TextureSlot.DIRT, Assets.get(dirt)).put(TextureSlot.TOP, Assets.get(block));
        var mappingMoist = new TextureMapping().put(TextureSlot.DIRT, Assets.get(dirt)).put(TextureSlot.TOP, Assets.moist(block));
        var dry = ModelTemplates.FARMLAND.create(block, mappingDry, modelOutput);
        var moist = ModelTemplates.FARMLAND.create(Assets.moist(block), mappingMoist, modelOutput);

        create(multiVariant(block).with(BlockModelGenerators.createEmptyOrFullDispatch(BlockStateProperties.MOISTURE, 7, moist, dry)));
    }

    public void dirtPath(Block block) {
        create(BlockModelGenerators.createRotatedVariant(block, Assets.getModelId(block)));
    }

    public void shortGrass(Block block) {
        gen.createCrossBlockWithDefaultItem(block, BlockModelGenerators.TintState.TINTED);
    }

    public void tallGrass(Block block) {
        gen.createDoublePlant(block, BlockModelGenerators.TintState.TINTED);
    }

    public void shortSeaGrass(Block block) {
        gen.createTrivialBlock(block, TexturedModel.SEAGRASS);
        gen.createSimpleFlatItemModel(block.asItem());
    }

    public void tallSeaGrass(Block block) {
        var top = gen.createSuffixedVariant(block, Assets.TOP_SUFFIX, ModelTemplates.SEAGRASS, TextureMapping::defaultTexture);
        var bottom = gen.createSuffixedVariant(block, Assets.DOWN_SUFFIX, ModelTemplates.SEAGRASS, TextureMapping::defaultTexture);

        gen.createDoubleBlock(block, top, bottom);
    }

    public void plant(Block block, BlockModelGenerators.TintState tint) {
        gen.createCrossBlockWithDefaultItem(block, tint);
    }

    public void plant(Block block, BlockModelGenerators.TintState tint, TextureMapping mapping) {
        gen.createCrossBlockWithDefaultItem(block, tint, mapping);
    }

    public void tallPlant(Block block, BlockModelGenerators.TintState tint) {
        gen.createDoublePlant(block, tint);
    }

    public void pottedPlant(Block block, Block potted, BlockModelGenerators.TintState tint) {
        gen.createPlant(block, potted, tint);
    }

    public void sunflower(Block block) {
        var top = Assets.getModelId(block, Assets.TOP_SUFFIX);
        var bottom = gen.createSuffixedVariant(block, Assets.DOWN_SUFFIX, BlockModelGenerators.TintState.NOT_TINTED.getCross(), TextureMapping::cross);

        gen.createSimpleFlatItemModel(block, Assets.FRONT_SUFFIX);
        gen.createDoubleBlock(block, top, bottom);
    }

    public void crossBlock(Block block, BlockModelGenerators.TintState tint, Property<Integer> property, int... propertyValues) {
        gen.createCrossBlock(block, tint, property, propertyValues);
    }

    public void crossBlock(Block block, BlockModelGenerators.TintState tint, TextureMapping mapping) {
        gen.createCrossBlock(block, tint, mapping);
    }

    public void crossBlock(Block block, BlockModelGenerators.TintState tint) {
        gen.createCrossBlock(block, tint);
    }

    public void flowerBed(Block block) {
        gen.createFlowerBed(block);
    }

    public void crop(Block block, Property<Integer> ageProperty, int... ageToVisualStageMapping) {
        gen.createCropBlock(block, ageProperty, ageToVisualStageMapping);
    }

    public void cocoa(Block block) {
        gen.createSimpleFlatItemModel(block.asItem());
        create(multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.AGE_2)
            .select(0, Variant.variant().with(VariantProperties.MODEL, Assets.getModelId(block, Assets.STAGE0_SUFFIX)))
            .select(1, Variant.variant().with(VariantProperties.MODEL, Assets.getModelId(block, Assets.STAGE1_SUFFIX)))
            .select(2, Variant.variant().with(VariantProperties.MODEL, Assets.getModelId(block, Assets.STAGE2_SUFFIX)))
        ).with(BlockModelGenerators.createHorizontalFacingDispatchAlt()));
    }

    public void pumpkin(Block block) {
        pillar(block, TextureMapping.column(block), PillarRotation.NONE);
    }

    public void pumpkinVariant(Block parent, Block block) {
        gen.createPumpkinVariant(block, TextureMapping.column(parent));
    }

    public void stems(Block unattached, Block attached) {
        gen.createStems(unattached, attached);
    }

    public void createPumpkinFamily(Block pumpkin, Block carvedPumpkin, Block stemUnattached, Block stemAttached) {
        pumpkin(pumpkin);
        pumpkinVariant(pumpkin, carvedPumpkin);
        stems(stemUnattached, stemAttached);
    }

    public void createPumpkinFamily(Block pumpkin, Block carvedPumpkin, Block carvedJackOLantern, Block stemUnattached, Block stemAttached) {
        createPumpkinFamily(pumpkin, carvedPumpkin, stemUnattached, stemAttached);
        pumpkinVariant(pumpkin, carvedJackOLantern);
    }

    public void pitcherPotCrop(Block block) {
        var properties = PropertyDispatch.properties(PitcherCropBlock.AGE, BlockStateProperties.DOUBLE_BLOCK_HALF).generate((age, half) ->
            Variant.variant().with(VariantProperties.MODEL, Assets.getModelId(block, (half == DoubleBlockHalf.UPPER? Assets.TOP_STAGE_AFFIX : Assets.DOWN_STAGE_AFFIX) + age)));

        gen.createSimpleFlatItemModel(block.asItem());
        create(multiVariant(block).with(properties));
    }

    public void pitcherPotPlant(Block block) {
        var top = Assets.getModelId(block, Assets.TOP_SUFFIX);
        var down = Assets.getModelId(block, Assets.DOWN_SUFFIX);

        gen.createSimpleFlatItemModel(block.asItem());
        gen.createDoubleBlock(block, top, down);
    }

    public void pitcherPot(Block crop, Block plant) {
        pitcherPotCrop(crop);
        pitcherPotPlant(plant);
    }

    public void bigDripleaf(Block block) {
        gen.skipAutoItemBlock(block);
        var dripleaf = Assets.getModelId(block);
        var partial = Assets.getModelId(block, Assets.PARTIAL_TILT_SUFFIX);
        var full = Assets.getModelId(block, Assets.FULL_TILT_SUFFIX);

        create(multiVariant(block).with(BlockModelGenerators.createHorizontalFacingDispatch()).with(PropertyDispatch.property(BlockStateProperties.TILT)
            .select(Tilt.NONE, Variant.variant().with(VariantProperties.MODEL, dripleaf))
            .select(Tilt.UNSTABLE, Variant.variant().with(VariantProperties.MODEL, dripleaf))
            .select(Tilt.PARTIAL, Variant.variant().with(VariantProperties.MODEL, partial))
            .select(Tilt.FULL, Variant.variant().with(VariantProperties.MODEL, full))
        ));
    }

    public void smallDripleaf(Block block) {
        gen.skipAutoItemBlock(block);
        var top = Assets.getModelId(block, Assets.TOP_SUFFIX);
        var bottom = Assets.getModelId(block, Assets.DOWN_SUFFIX);

        create(multiVariant(block).with(BlockModelGenerators.createHorizontalFacingDispatch()).with(PropertyDispatch.property(BlockStateProperties.DOUBLE_BLOCK_HALF)
            .select(DoubleBlockHalf.LOWER, Variant.variant().with(VariantProperties.MODEL, bottom))
            .select(DoubleBlockHalf.UPPER, Variant.variant().with(VariantProperties.MODEL, top))
        ));
    }

    public void coralFans(Block fan, Block fanWall) {
        gen.createCoralFans(fan, fanWall);
    }

    public void createCoralFamily(Block coralBlock, Block deadCoralBlock, Block coralFullBlock, Block deadCoralFullBlock, Block coralFanBlock, Block deadCoralFanBlock, Block coralWallFanBlock, Block deadCoralWallFanBlock) {
        gen.createCoral(coralBlock, deadCoralBlock, coralFullBlock, deadCoralFullBlock, coralFanBlock, deadCoralFanBlock, coralWallFanBlock, deadCoralWallFanBlock);
    }

    public void craftingTableLike(Block block, Block bottom, BiFunction<Block, Block, TextureMapping> mapping) {
        gen.createCraftingTableLike(block, bottom, mapping);
    }

    public void craftingTable(Block block, Block bottom) {
        craftingTableLike(block, bottom, TextureMapping::craftingTable);
    }

    public void fetchingTable(Block block, Block bottom) {
        craftingTableLike(block, bottom, TextureMapping::fletchingTable);
    }

    public void cartographyTable(Block block, Block bottom) {
        cube(block,
            Assets.top(block),
            Assets.get(bottom),
            Assets.get(block, Assets.SIDE3_SUFFIX),
            Assets.get(block, Assets.SIDE1_SUFFIX),
            Assets.get(block, Assets.SIDE2_SUFFIX),
            Assets.get(block, Assets.SIDE3_SUFFIX),
            Assets.get(block, Assets.SIDE3_SUFFIX)
        );
    }

    public void smithingTable(Block block, Block bottom) {
        cube(block,
            Assets.top(block),
            Assets.get(bottom),
            Assets.front(block),
            Assets.front(block),
            Assets.side(block),
            Assets.side(block),
            Assets.front(block)
        );
    }

    public void furnace(Block block, TexturedModel.Provider texturedModel) {
        gen.createFurnace(block, texturedModel);
    }

    /*TODO
    Chest
    Enchantment Table
    Anvil
    Grindstone
    Cauldron
    Dispenser
    Dropper
    Composter
    Bell

    And more O/-<
     */
}