package net.justmili.mcl.core.mixin;

import net.justmili.mcl.v1.api.events.server.PlayerEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceResultSlot.class)
public class FurnaceResultSlotMixin {

    @Shadow
    @Final
    private Player player;

    @Inject(method = "checkTakeAchievements", at = @At(value = "TAIL"))
    private void corelibs$smelt(ItemStack stack, CallbackInfo ci) {
        PlayerEvents.SMELT_ITEM.invoker().onSmeltItem(player, stack);
    }
}
