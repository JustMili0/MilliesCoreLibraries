package net.justmili.corelibs.core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.justmili.corelibs.v1.api.events.server.PlayerEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @ModifyReturnValue(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("RETURN"))
    private ItemEntity corelibs$drop(ItemEntity original) {
        if (original != null) {
            var result = PlayerEvents.DROP_ITEM.invoker().onDropItem((ServerPlayer) (Object) this, original);
            if (!result) return null;
        }
        return original;
    }
}
