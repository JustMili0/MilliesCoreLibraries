package net.justmili.mcl.core.mixin;

import net.justmili.mcl.v1.api.events.server.PlayerAdvancementEvents;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {

    @Shadow
    private ServerPlayer player;

    @Inject(at = @At("HEAD"), method = "startProgress")
    private void startProgressMixin(Advancement advancement, AdvancementProgress advancementProgress, CallbackInfo ci) {
        PlayerAdvancementEvents.START_PROGRESS.invoker().startProgress(player, advancement, advancementProgress);
    }

    @Inject(at = @At("HEAD"), method = "award")
    private void onAdvancementAward(Advancement advancement, String string, CallbackInfoReturnable<Boolean> cir) {
        PlayerAdvancementEvents.AWARD.invoker().awardAdvancement(player, advancement);
        player.sendSystemMessage(Component.literal("advancement " + advancement.getId()));
    }

    @Inject(at = @At("HEAD"), method = "revoke")
    private void onAdvancementRevoke(Advancement advancement, String string, CallbackInfoReturnable<Boolean> cir) {
        PlayerAdvancementEvents.REVOKE.invoker().revokeAdvancement(player, advancement);
    }

}
