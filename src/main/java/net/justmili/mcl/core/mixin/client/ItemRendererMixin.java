package net.justmili.mcl.core.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import net.justmili.mcl.v1.api.events.client.ItemRenderEvents;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @ModifyVariable(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private BakedModel corelibs$modifyModel(BakedModel original, ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        return ItemRenderEvents.MODIFY_MODEL.invoker().onModifyModel(stack, context, pose, original);
    }

    @ModifyReturnValue(method = "getModel", at = @At("RETURN"))
    private BakedModel corelibs$modifyResolvedModel(BakedModel original, ItemStack stack, @Nullable Level level, @Nullable LivingEntity entity, int seed) {
        return ItemRenderEvents.MODIFY_RESOLVED_MODEL.invoker().onModifyResolvedModel(stack, level, entity, original);
    }
}