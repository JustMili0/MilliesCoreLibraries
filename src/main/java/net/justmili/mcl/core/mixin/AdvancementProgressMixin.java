package net.justmili.mcl.core.mixin;

import net.justmili.mcl.v1.api.events.server.AdvancementEvents;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Criterion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(AdvancementProgress.class)
public class AdvancementProgressMixin {

    @Inject(method = "update", at = @At("TAIL"))
    private void corelibs$updateProgress(Map<String, Criterion> criteria, String[][] requirements, CallbackInfo ci) {
        AdvancementEvents.UPDATE_PROGRESS.invoker().onUpdateProgress((AdvancementProgress) (Object) this, criteria, requirements);
    }
}