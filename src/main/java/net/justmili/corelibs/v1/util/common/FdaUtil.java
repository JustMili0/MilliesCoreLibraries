package net.justmili.corelibs.v1.util.common;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

@SuppressWarnings({"UnstableApiUsage"})
public class FdaUtil {
    public static boolean getBool(AttachmentTarget target, AttachmentType<Boolean> variable) {
        return get(target, variable, false);
    }

    public static String getString(AttachmentTarget target, AttachmentType<String> variable) {
        return get(target, variable, "ValueReturnedNull");
    }

    public static int getInt(AttachmentTarget target, AttachmentType<Integer> variable) {
        return get(target, variable, -1);
    }

    public static double getDouble(AttachmentTarget target, AttachmentType<Double> variable) {
        return get(target, variable, -1.0);
    }

    public static float getFloat(AttachmentTarget target, AttachmentType<Float> variable) {
        return get(target, variable, -1f);
    }

    public static long getLong(AttachmentTarget target, AttachmentType<Long> variable) {
        return get(target, variable, -1L);
    }

    // Generic value getter and setters
    public static <A> void set(AttachmentTarget target, AttachmentType<A> variable, A value) {
        target.setAttached(variable, value);
    }

    public static <A> A get(AttachmentTarget target, AttachmentType<A> variable, A defaultValue) {
        return target.getAttachedOrElse(variable, defaultValue);
    }

    /// Will silently throw and return nothing if target does not have the variable
    /// It is preferred to use get(target, variable, defaultValue)
    public static <A> A get(AttachmentTarget target, AttachmentType<A> variable) {
        return target.getAttachedOrThrow(variable);
    }

    public static <A> void remove(AttachmentTarget target, AttachmentType<A> variable) {
        target.removeAttached(variable);
    }

    public static <A> void modify(AttachmentTarget target, AttachmentType<A> variable, UnaryOperator<A> operator) {
        target.modifyAttached(variable, operator);
    }

    public static <A> boolean has(AttachmentTarget target, AttachmentType<A> variable) {
        return target.hasAttached(variable);
    }

    // Creates values that will clear after a restart
    public static <A> AttachmentType<A> createTransient(ResourceLocation id, A defaultValue) {
        return AttachmentRegistry.<A>builder().initializer(() -> defaultValue).copyOnDeath().buildAndRegister(id);
    }

    // Creates values that will not clear after a restart
    public static <A> AttachmentType<A> createPersistent(ResourceLocation id, A defaultValue, Codec<A> codec) {
        return AttachmentRegistry.<A>builder().initializer(() -> defaultValue).copyOnDeath().persistent(codec).buildAndRegister(id);
    }

    // Creates values synced to the player and clear after restart
    // CORELIBS: synced added in 1.21.4, unavailable in 1.20.1
//    public static <A> AttachmentType<A> createSynced(ResourceLocation id, A defaultValue, StreamCodec<? super RegistryFriendlyByteBuf, A> streamCodec) {
//        return AttachmentRegistry.<A>builder().initializer(() -> defaultValue).copyOnDeath(); // ???
//        //return AttachmentRegistry.create(id, builder -> builder.initializer(() -> defaultValue).copyOnDeath().syncWith(streamCodec, AttachmentSyncPredicate.targetOnly()));
//    }
}