package net.justmili.mcl.core.mixin;

import net.justmili.mcl.v1.api.events.server.TickEvents;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void corelibs$tickPre(CallbackInfo ci) {
        TickEvents.LIVING_ENTITY_PRE.invoker().onStartTick((LivingEntity) (Object) this);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void corelibs$tickPost(CallbackInfo ci) {
        TickEvents.LIVING_ENTITY_POST.invoker().onEndTick((LivingEntity) (Object) this);
    }
}
