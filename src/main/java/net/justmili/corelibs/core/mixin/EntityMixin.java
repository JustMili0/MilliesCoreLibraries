package net.justmili.corelibs.core.mixin;

import net.justmili.corelibs.v1.api.events.server.TickEvents;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void corelibs$tickPre(CallbackInfo ci) {
        TickEvents.ENTITY_PRE.invoker().onStartTick((Entity) (Object) this);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void corelibs$tickPost(CallbackInfo ci) {
        TickEvents.ENTITY_POST.invoker().onEndTick((Entity) (Object) this);
    }
}
