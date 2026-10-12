package net.justmili.corelibs.core.mixin.forge;

import net.justmili.corelibs.api.events.server.PlayerEvents;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void onCopyFrom(ServerPlayer that, boolean keepEverything, CallbackInfo ci) {
        PlayerEvents.CLONE.invoker().onClone(that, (ServerPlayer) (Object) this, keepEverything);
    }
}
