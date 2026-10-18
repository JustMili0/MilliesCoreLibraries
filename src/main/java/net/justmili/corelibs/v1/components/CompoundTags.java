//? if < 1.20.5 {
package net.justmili.corelibs.v1.components;

import net.minecraft.world.item.ItemStack;

@Deprecated(since = "1.20.5")
public final class CompoundTags {
    private CompoundTags() {
    }

    public static final String ITEM_ID = "id";
    public static final String ITEM_COUNT = "Count";
    public static final String ITEM_TAG = "tag";
    public static final String DAMAGE = "Damage";
    public static final String UNBREAKABLE = "Unbreakable";
    public static final String DISPLAY = "display";
    public static final String CUSTOM_NAME = "Name";
    public static final String LORE = "Lore";
    public static final String DYED_COLOR = "color";
    public static final String MAP_COLOR = "MapColor";
    public static final String ENCHANTMENTS = "Enchantments";
    public static final String STORED_ENCHANTMENTS = "StoredEnchantments";
    public static final String CAN_PLACE_ON = "CanPlaceOn";
    public static final String CAN_BREAK = "CanDestroy";
    public static final String ATTRIBUTE_MODIFIERS = "AttributeModifiers";
    public static final String TOOLTIP_DISPLAY = "HideFlags";
    public static final String REPAIR_COST = "RepairCost";
    public static final String POTION_CONTENTS = "Potion";
    public static final String POTION_CUSTOM_COLOR = "CustomPotionColor";
    public static final String POTION_CUSTOM_EFFECTS = "CustomPotionEffects";
    public static final String SUSPICIOUS_STEW_EFFECTS = "Effects";
    public static final String SUSPICIOUS_STEW_EFFECT_ID = "EffectId";
    public static final String SUSPICIOUS_STEW_EFFECT_DURATION = "EffectDuration";
    public static final String CHARGED = "Charged";
    public static final String CHARGED_PROJECTILES = "ChargedProjectiles";
    public static final String FIREWORKS = "Fireworks";
    public static final String FIREWORK_EXPLOSION = "Explosion";
    public static final String FIREWORK_EXPLOSIONS = "Explosions";
    public static final String FIREWORK_FLIGHT = "Flight";
    public static final String FIREWORK_TYPE = "Type";
    public static final String FIREWORK_TRAIL = "Trail";
    public static final String FIREWORK_FLICKER = "Flicker";
    public static final String FIREWORK_COLORS = "Colors";
    public static final String FIREWORK_FADE_COLORS = "FadeColors";
    public static final String MAP_ID = "map";
    public static final String MAP_DECORATIONS = "Decorations";
    public static final String MAP_SCALE_DIRECTION = "map_scale_direction";
    public static final String MAP_TO_LOCK = "map_to_lock";
    public static final String LODESTONE_TRACKER_POS = "LodestonePos";
    public static final String LODESTONE_TRACKER_DIMENSION = "LodestoneDimension";
    public static final String LODESTONE_TRACKER_TRACKED = "LodestoneTracked";
    public static final String BANNER_PATTERNS = "Patterns";
    public static final String BANNER_PATTERN = "Pattern";
    public static final String BANNER_COLOR = "Color";
    public static final String BLOCK_CUSTOM_NAME = "CustomName";
    public static final String POT_DECORATIONS = "sherds";
    public static final String SIGN_TEXT_FRONT = "front_text";
    public static final String SIGN_TEXT_BACK = "back_text";
    public static final String WAXED = "is_waxed";
    public static final String BEES = "Bees";
    public static final String BEE_ENTITY_DATA = "EntityData";
    public static final String BEE_TICKS_IN_HIVE = "TicksInHive";
    public static final String BEE_MIN_OCCUPATION_TICKS = "MinOccupationTicks";
    public static final String BEE_FLOWER_POS = "FlowerPos";
    public static final String BEE_HAS_NECTAR = "HasNectar";

    public static final class AttributeModifier {
        public static final String ATTRIBUTE = "AttributeName";
        public static final String SLOT = "Slot";

        private AttributeModifier() {
        }
    }

    public static final class MapDecoration {
        public static final String ID = "id";
        public static final String TYPE = "type";
        public static final String X = "x";
        public static final String Z = "z";
        public static final String ROTATION = "rot";

        private MapDecoration() {
        }
    }

    public static final class HideFlags {
        public static final int ENCHANTMENTS = 1;
        public static final int ATTRIBUTE_MODIFIERS = 2;
        public static final int UNBREAKABLE = 4;
        public static final int CAN_BREAK = 8;
        public static final int CAN_PLACE_ON = 16;
        public static final int ADDITIONAL = 32;
        public static final int DYED_COLOR = 64;
        public static final int UPGRADES = 128;

        private HideFlags() {
        }
    }

    public boolean isAlwaysEdible(ItemStack stack) {
        var props = stack.getItem().getFoodProperties();
        return props != null && props.canAlwaysEat();
    }
}
//?}