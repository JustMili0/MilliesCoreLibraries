package net.justmili.mcl.core.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.justmili.mcl.v1.api.events.client.ItemRenderEvents;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.item.ItemEntity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {

    @ModifyExpressionValue(method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/math/Axis;rotation(F)Lorg/joml/Quaternionf;"))
    private Quaternionf corelibs$modifyRotation(Quaternionf original, ItemEntity entity, float entityYaw, float partialTick) {
        return ItemRenderEvents.MODIFY_ENTITY_ROTATION.invoker().onModifyRotation(entity, partialTick, original);
    }
}