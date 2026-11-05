package net.justmili.mcl.v1.api.events.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class BlockRenderEvents {

    public static final Event<ModifyBatchedModel> MODIFY_BATCHED_MODEL = EventFactory.createArrayBacked(ModifyBatchedModel.class, callbacks -> (state, pos, level, model) -> {
        for (var event : callbacks) {
            model = event.onModifyBatchedModel(state, pos, level, model);
        }
        return model;
    });

    @FunctionalInterface
    public interface ModifyBatchedModel {
        BakedModel onModifyBatchedModel(BlockState state, BlockPos pos, BlockAndTintGetter level, BakedModel model);
    }
}