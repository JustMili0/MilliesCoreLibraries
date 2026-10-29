package net.justmili.corelibs.core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.justmili.corelibs.v1.api.events.server.PlayerEvents;
import net.justmili.corelibs.v1.api.events.server.TickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void corelibs$tickPre(CallbackInfo ci) {
        TickEvents.PLAYER_PRE.invoker().onStartTick((ServerPlayer) (Object) this);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void corelibs$tickPost(CallbackInfo ci) {
        TickEvents.PLAYER_POST.invoker().onEndTick((ServerPlayer) (Object) this);
    }

    @ModifyReturnValue(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("RETURN"))
    private ItemEntity corelibs$drop(ItemEntity original) {
        if (original != null) {
            var result = PlayerEvents.DROP_ITEM.invoker().onDropItem((ServerPlayer) (Object) this, original);
            if (!result) return null;
        }
        return original;
    }
}
