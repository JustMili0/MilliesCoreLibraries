package net.justmili.mcl.core.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.justmili.mcl.v1.api.events.client.BlockRenderEvents;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockRenderDispatcher.class)
public class BlockRenderDispatcherMixin {

    @ModifyExpressionValue(method = {
        "renderBatched", // fabric target
        "renderBatched*" // forge target (for later forge port)
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;getBlockModel(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/resources/model/BakedModel;"), require = 0)
    private BakedModel corelibs$modifyBatchedModel(BakedModel original, BlockState state, BlockPos pos, BlockAndTintGetter level) {
        return BlockRenderEvents.MODIFY_BATCHED_MODEL.invoker().onModifyBatchedModel(level, state, pos, original);
    }
}