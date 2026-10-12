package net.justmili.corelibs.core.mixin;

import net.justmili.corelibs.api.events.server.PlayerEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void corelibs$callPlayerPickupEvent(Player player, CallbackInfo ci) {
        if (!PlayerEvents.CAN_PICKUP_ITEM.invoker().canPickupItem(player, (ItemEntity) (Object) this, this.getItem())) ci.cancel();
    }
}