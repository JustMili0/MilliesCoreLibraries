package net.justmili.corelibs.v1.util.client;

import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemRendererUtil {
    public record Entry(Item item, ModelIdentifier otherItemModel, List<ItemDisplay> displayAt) {
    }
    public enum ItemDisplay {FIRST_PERSON, THIRD_PERSON, HEAD, GUI, FIXED, ON_SHELF, ON_GROUND}
    private static final Set<Entry> OVERRIDES = new HashSet<>();

    public static void renderAs(Item item, ModelIdentifier otherItemModel, ItemDisplay... displayAt) {
        OVERRIDES.add(new Entry(item, otherItemModel, List.of(displayAt)));
    }

    public static ItemDisplay toItemDisplay(ItemDisplayContext context) {
        return switch (context) {
            case GUI -> ItemDisplay.GUI;
            case GROUND -> ItemDisplay.ON_GROUND;
            case FIXED -> ItemDisplay.FIXED;
            case HEAD -> ItemDisplay.HEAD;
            case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND -> ItemDisplay.FIRST_PERSON;
            case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> ItemDisplay.THIRD_PERSON;
            default -> null;
        };
    }

    public static Entry getOverrideItem(Item item) {
        for (var entry : OVERRIDES) {
            if (entry.item() == item) return entry;
        }
        return null;
    }
}