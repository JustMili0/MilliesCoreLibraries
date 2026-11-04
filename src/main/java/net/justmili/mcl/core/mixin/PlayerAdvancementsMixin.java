package net.justmili.mcl.core.mixin;

import net.justmili.mcl.v1.api.events.server.AdvancementEvents;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// CORELIBS TODO: Mojang did something with advancements in newer versions, needs checking after we're done with 1.20.1 dev
@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {

    @Shadow
    private ServerPlayer player;

    @Inject(method = "reload", at = @At("TAIL"))
    private void corelibs$reload(ServerAdvancementManager manager, CallbackInfo ci) {
        AdvancementEvents.RELOAD.invoker().onReload(player, manager);
    }

    @Inject(method = "startProgress", at = @At("TAIL"))
    private void corelibs$startProgress(Advancement advancement, AdvancementProgress progress, CallbackInfo ci) {
        AdvancementEvents.START_PROGRESS.invoker().onStartProgress(player, advancement, progress);
    }

    @Inject(method = "award", at = @At("TAIL"))
    private void corelibs$award(Advancement advancement, String criterion, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) return;
        AdvancementEvents.AWARD.invoker().onAward(player, advancement);
    }

    @Inject(method = "revoke", at = @At("TAIL"))
    private void corelibs$revoke(Advancement advancement, String criterion, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) return;
        AdvancementEvents.REVOKE.invoker().onRevoke(player, advancement);
    }
}