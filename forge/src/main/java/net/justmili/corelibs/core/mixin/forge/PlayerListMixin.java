package net.justmili.corelibs.core.mixin.forge;

import net.justmili.corelibs.api.events.server.PlayerEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Inject(method = "respawn", at = @At("TAIL"))
    private void corelibs$afterRespawn(ServerPlayer player, boolean keepEverything, CallbackInfoReturnable<ServerPlayer> cir) {
        PlayerEvents.RESPAWN.invoker().onRespawn(player, cir.getReturnValue(), keepEverything);
    }
}
