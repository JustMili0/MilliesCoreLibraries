package net.justmili.mcl.core.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.justmili.mcl.v1.api.events.server.PlayerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import org.spongepowered.asm.mixin.*;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

    @Shadow
    protected ServerLevel level;

    @Shadow
    @Final
    protected ServerPlayer player;

    // Hook up own event for block breaking because Fabric API's event doesn't fully provide what we want
    @WrapMethod(method = "destroyBlock")
    private boolean corelibs$blockBrokenEvent(BlockPos pos, Operation<Boolean> original) {
        // Save the BlockState before the block is destroyed, since afterwards it's gone
        var state = level.getBlockState(pos);
        boolean result = original.call(pos);

        PlayerEvents.BREAK_BLOCK.invoker().onBlockBroken(level, player, state, level.getBlockState(pos), pos, result);
        return result;
    }
}