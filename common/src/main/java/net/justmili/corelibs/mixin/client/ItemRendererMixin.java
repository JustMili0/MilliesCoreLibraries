package net.justmili.corelibs.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.justmili.util.utils.client.ItemRendererUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @ModifyVariable(
        method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
        at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private BakedModel corelibs$applyRenderOverride(BakedModel original, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        var renderer = (ItemRenderer)(Object)this;
        var heldItem = itemStack.getItem();

        var override = ItemRendererUtil.getOverrideItem(heldItem);
        if (override == null) return original;

        var mapped = ItemRendererUtil.toItemDisplay(displayContext);
        if (mapped != null && override.displayAt().contains(mapped)) return renderer.getItemModelShaper().getModelManager().getModel(override.otherItemModel());

        return original;
    }
}