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

    public static final Event<ModifyResolvedModel> MODIFY_RESOLVED_MODEL = EventFactory.createArrayBacked(ModifyResolvedModel.class, callbacks -> (stack, level, entity, model) -> {
        for (var event : callbacks) {
            model = event.onModifyResolvedModel(stack, level, entity, model);
        }
        return model;
    });

    public static final Event<ModifyModel> MODIFY_MODEL = EventFactory.createArrayBacked(ModifyModel.class, callbacks -> (stack, context, pose, model) -> {
        for (var event : callbacks) {
            model = event.onModifyModel(stack, context, pose, model);
        }
        return model;
    });

    public static final Event<ModifyEntityRotation> MODIFY_ENTITY_ROTATION = EventFactory.createArrayBacked(ModifyEntityRotation.class, callbacks -> (entity, partialTick, rotation) -> {
        for (var event : callbacks) {
            rotation = event.onModifyRotation(entity, partialTick, rotation);
        }
        return rotation;
    });

    @FunctionalInterface
    public interface ModifyResolvedModel {
        BakedModel onModifyResolvedModel(ItemStack stack, @Nullable Level level, @Nullable LivingEntity entity, BakedModel model);
    }

    @FunctionalInterface
    public interface ModifyModel {
        BakedModel onModifyModel(ItemStack stack, ItemDisplayContext context, PoseStack pose, BakedModel model);
    }

    @FunctionalInterface
    public interface ModifyEntityRotation {
        Quaternionf onModifyRotation(ItemEntity entity, float partialTick, Quaternionf rotation);
    }
}