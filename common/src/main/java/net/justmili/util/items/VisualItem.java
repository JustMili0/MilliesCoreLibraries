package net.justmili.util.items;

//import net.minecraft.client.gui.GuiGraphicsExtractor;
//import net.minecraft.core.Holder;
//import net.minecraft.core.component.DataComponentMap;
//import net.minecraft.core.component.DataComponentPatch;
//import net.minecraft.core.component.DataComponents;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.resources.Identifier;
//import net.minecraft.world.item.ItemStack;

public final class VisualItem {
//    private final Identifier id;
//    private final DataComponentPatch patch;
//    private final int seed;
//    private final int count;
//    private ItemStack stack = null;
//
//    public VisualItem(Identifier id) {
//        this(id, DataComponentPatch.EMPTY, 0, 1);
//    }
//
//    public VisualItem(Identifier id, DataComponentPatch patch, int seed, int count) {
//        this.id = id;
//        this.patch = patch;
//        this.seed = seed;
//        this.count = count;
//    }
//
//    public void render(GuiGraphicsExtractor graphics, int x, int y) {
//        graphics.item(createItemStack(), x, y, this.seed);
//    }
//
//    private ItemStack createItemStack() {
//        if (this.stack == null) {
//            this.stack = new ItemStack(Holder.direct(
//                BuiltInRegistries.ITEM.getValue(this.id),
//                DataComponentMap.builder()
//                    .set(DataComponents.MAX_STACK_SIZE, 64)
//                    .set(DataComponents.ITEM_MODEL, this.id)
//                    .build()
//            ),
//                this.count,
//                this.patch
//            );
//        }
//
//        return this.stack;
//    }
//
//    public Identifier id() {
//        return this.id;
//    }
}