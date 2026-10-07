package net.justmili.corelibs.mixin;

import net.justmili.api.events.server.UseEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void corelibs$handleDietItemInteraction(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        // Ironically, out of all the events available in *both* Fabric and NeoForge,
        // none of them handle this specific use case.
        var result = UseEvents.ITEM_PRE.invoker().onUseItemPre(level, player, hand);
        if (result.getResult() != InteractionResult.PASS) cir.setReturnValue(result);
    }
}
