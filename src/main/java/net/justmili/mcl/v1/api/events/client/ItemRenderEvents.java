package net.justmili.mcl.v1.api.events.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class ItemRenderEvents {

    public static final Event<ModifyResolvedModel> MODIFY_RESOLVED_MODEL = EventFactory.createArrayBacked(ModifyResolvedModel.class, callbacks -> (level, entity, stack, originalModel) -> {
        for (var event : callbacks) {
            originalModel = event.onModifyResolvedModel(level, entity, stack, originalModel);
        }
        return originalModel;
    });

    public static final Event<ModifyModel> MODIFY_MODEL = EventFactory.createArrayBacked(ModifyModel.class, callbacks -> (stack, originalModel, pose, context) -> {
        for (var event : callbacks) {
            originalModel = event.onModifyModel(stack, originalModel, pose, context);
        }
        return originalModel;
    });

    public static final Event<ModifyEntityRotation> MODIFY_ENTITY_ROTATION = EventFactory.createArrayBacked(ModifyEntityRotation.class, callbacks -> (entity, originalRot, partialTick) -> {
        for (var event : callbacks) {
            originalRot = event.onModifyRotation(entity, originalRot, partialTick);
        }
        return originalRot;
    });

    @FunctionalInterface
    public interface ModifyResolvedModel {
        BakedModel onModifyResolvedModel(@Nullable Level level, @Nullable LivingEntity entity, ItemStack stack, BakedModel originalModel);
    }

    @FunctionalInterface
    public interface ModifyModel {
        BakedModel onModifyModel(ItemStack stack, BakedModel originalModel, PoseStack pose, ItemDisplayContext context);
    }

    @FunctionalInterface
    public interface ModifyEntityRotation {
        Quaternionf onModifyRotation(ItemEntity entity, Quaternionf originalRot, float partialTick);
    }
}