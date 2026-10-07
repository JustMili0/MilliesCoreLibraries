package net.justmili.util.utils.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;


public final class NbtUtil {
    private NbtUtil() {
    }

    public static CustomDataBuilder custom() {
        return new CustomDataBuilder();
    }

    public static final class CustomDataBuilder {
        private final CompoundTag tag = new CompoundTag();

        private CustomDataBuilder() {
        }

        public CustomDataBuilder addBool(String key, boolean value) {
            tag.putBoolean(key, value);
            return this;
        }

        public CustomDataBuilder addInt(String key, int value) {
            tag.putInt(key, value);
            return this;
        }

        public CustomDataBuilder addLong(String key, long value) {
            tag.putLong(key, value);
            return this;
        }

        public CustomDataBuilder addDouble(String key, double value) {
            tag.putDouble(key, value);
            return this;
        }

        public CustomDataBuilder addFloat(String key, float value) {
            tag.putFloat(key, value);
            return this;
        }

        public CustomDataBuilder addString(String key, String value) {
            tag.putString(key, value);
            return this;
        }

        public ItemStack applyToStack(ItemStack stack) {
            stack.getOrCreateTag().merge(tag);
            return stack;
        }

        public ItemStack overwriteStack(ItemStack stack) {
            stack.setTag(tag.copy());
            return stack;
        }
    }

    public static boolean getBool(ItemStack stack, String key, boolean orElse) {
        var tag = tagOf(stack);
        return tag.contains(key, Tag.TAG_ANY_NUMERIC)? tag.getBoolean(key) : orElse;
    }

    public static int getInt(ItemStack stack, String key, int orElse) {
        var tag = tagOf(stack);
        return tag.contains(key, Tag.TAG_ANY_NUMERIC)? tag.getInt(key) : orElse;
    }

    public static long getLong(ItemStack stack, String key, long orElse) {
        var tag = tagOf(stack);
        return tag.contains(key, Tag.TAG_ANY_NUMERIC)? tag.getLong(key) : orElse;
    }

    public static double getDouble(ItemStack stack, String key, double orElse) {
        var tag = tagOf(stack);
        return tag.contains(key, Tag.TAG_ANY_NUMERIC)? tag.getDouble(key) : orElse;
    }

    public static float getFloat(ItemStack stack, String key, float orElse) {
        var tag = tagOf(stack);
        return tag.contains(key, Tag.TAG_ANY_NUMERIC)? tag.getFloat(key) : orElse;
    }

    public static String getString(ItemStack stack, String key, String orElse) {
        var tag = tagOf(stack);
        return tag.contains(key, Tag.TAG_STRING)? tag.getString(key) : orElse;
    }

    public static boolean has(ItemStack stack, String key) {
        return stack.hasTag() && stack.getTag().contains(key);
    }

    public static void remove(ItemStack stack, String key) {
        if (stack.hasTag()) stack.getTag().remove(key);
    }

    private static CompoundTag tagOf(ItemStack stack) {
        var tag = stack.getTag();
        return tag == null? new CompoundTag() : tag;
    }
}