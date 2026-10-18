package net.justmili.corelibs.core.mixin;

import net.justmili.corelibs.libs.api.events.server.PlayerEvents;
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

    @Unique
    private BlockState corelibs$oldState;

    // Save the BlockState before the block is destroyed, since afterwards it's gone
    @Inject(method = "destroyBlock", at = @At("HEAD"))
    private void corelibs$captureStateBefore(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        corelibs$oldState = level.getBlockState(pos);
    }

    // Hook up own event for block breaking because Fabric API's event doesn't fully provide what we want
    @Inject(method = "destroyBlock", at = @At("RETURN"))
    private void corelibs$blockBrokenEvent(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        PlayerEvents.BREAK_BLOCK.invoker().onBlockBroken(level, player, corelibs$oldState, level.getBlockState(pos), pos, cir.getReturnValueZ());
        corelibs$oldState = null;
    }
}