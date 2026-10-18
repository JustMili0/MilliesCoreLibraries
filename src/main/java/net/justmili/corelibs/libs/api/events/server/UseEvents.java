package net.justmili.corelibs.libs.api.events.server;

import net.justmili.corelibs.libs.api.events.Event;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Centralized group of Fabric's "use callback" events,
 * with an extra {@link #ITEM_PRE} event for item use behavior
 * that Fabric, Quilt, Forge and NeoForge don't cover.
 *
 * <p>Everything except {@link #ITEM_PRE} is directly connected to Fabric API events
 */
public class UseEvents {
    private UseEvents() {
    }

    /**
     * An event that is called before an item is used.
     *
     * <p>TODO: Finish this javadoc
     */
    public static final Event<ItemPre> ITEM_PRE = Event.create(ItemPre.class, callbacks -> (level, player, hand) -> {
        for (var event : callbacks) {
            var result = event.onUseItemPre(level, player, hand);
            if (result.getResult() != InteractionResult.PASS) return result;
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    });

    public static final Event<ItemPost> ITEM_POST = Event.create(ItemPost.class, callbacks -> (level, player, hand, stack) -> {
        for (var event : callbacks) event.onUseItemPost(level, player, hand, stack);
    });

    public static final Event<BlockPost> BLOCK_POST = Event.create(BlockPost.class, callbacks -> (level, player, hand, pos, state, result) -> {
        for (var event : callbacks) event.onUseBlockPost(level, player, hand, pos, state, result);
    });

    public static final Event<EntityPost> ENTITY_POST = Event.create(EntityPost.class, callbacks -> (level, player, hand, entity, result) -> {
        for (var event : callbacks) event.onUseEntityPost(level, player, hand, entity, result);
    });

    @FunctionalInterface
    public interface ItemPre {
        /**
         * Called before an item is used.
         *
         * @param level  the level the player is in
         * @param player the player using the item
         * @param hand   the hand holding the item
         * @return {@link InteractionResultHolder#pass(Object)} to let the item behave normally,
         * or any other result to override it
         */
        InteractionResultHolder<ItemStack> onUseItemPre(Level level, Player player, InteractionHand hand);
    }

    @FunctionalInterface
    public interface ItemPost {
        void onUseItemPost(Level level, Player player, InteractionHand hand, ItemStack stack);
    }

    @FunctionalInterface
    public interface BlockPost {
        void onUseBlockPost(Level level, Player player, InteractionHand hand, BlockPos pos, BlockState state, InteractionResult result);
    }

    @FunctionalInterface
    public interface EntityPost {
        void onUseEntityPost(Level level, Player player, InteractionHand hand, Entity entity, InteractionResult result);
    }
}