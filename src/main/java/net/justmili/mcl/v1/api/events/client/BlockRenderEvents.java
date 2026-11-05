package net.justmili.mcl.v1.api.events.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class BlockRenderEvents {

    public static final Event<ModifyBatchedModel> MODIFY_BATCHED_MODEL = EventFactory.createArrayBacked(ModifyBatchedModel.class, callbacks -> (blockGetter, state, pos, originalModel) -> {
        for (var event : callbacks) {
            originalModel = event.onModifyBatchedModel(blockGetter, state, pos, originalModel);
        }
        return originalModel;
    });

    @FunctionalInterface
    public interface ModifyBatchedModel {
        BakedModel onModifyBatchedModel(BlockAndTintGetter blockGetter, BlockState state, BlockPos pos, BakedModel originalModel);
    }
}