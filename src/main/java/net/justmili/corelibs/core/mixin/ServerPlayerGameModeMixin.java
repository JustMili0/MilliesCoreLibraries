package net.justmili.corelibs.core.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.justmili.corelibs.v1.api.events.server.PlayerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

    @Shadow
    protected ServerLevel level;

    @Shadow
    @Final
    protected ServerPlayer player;

    // Hook up own event for block breaking because Fabric API's event doesn't fully provide what we want
    @WrapMethod(method = "destroyBlock")
    private boolean vnt$blockBrokenEvent(BlockPos pos, Operation<Boolean> original) {
        // Save the BlockState before the block is destroyed, since afterwards it's gone
        var state = level.getBlockState(pos);
        boolean result = original.call(pos);

        PlayerEvents.BREAK_BLOCK.invoker().onBlockBroken(level, player, state, level.getBlockState(pos), pos, result);
        return result;
    }
}