package net.justmili.mcl.v1.api.events.server;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Centralized group of Fabric's "use callback" events, with an extra {@link #ITEM_PRE} event for item use behavior that Fabric, Quilt, Forge and NeoForge don't cover.
 *
 * <p>Everything except {@link #ITEM_PRE} is directly connected to Fabric API events
 */
public class UseEvents {

    /**
     * An event that is called before an item is used.
     *
     * <p>TODO: Finish this javadoc
     */
    public static final Event<ItemPre> ITEM_PRE = EventFactory.createArrayBacked(ItemPre.class, callbacks -> (level, player, hand) -> {
        for (var event : callbacks) {
            var result = event.onUseItemPre(level, player, hand);
            if (result.getResult() != InteractionResult.PASS) return result;
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    });

    public static final Event<ItemPost> ITEM_POST = EventFactory.createArrayBacked(ItemPost.class, callbacks -> (level, player, hand, stack) -> {
        for (var event : callbacks) {
            var result = event.onUseItemPost(level, player, hand, stack);
            if (result != InteractionResult.PASS) return result;
        }
        return InteractionResult.PASS;
    });

    public static final Event<BlockPost> BLOCK_POST = EventFactory.createArrayBacked(BlockPost.class, callbacks -> (level, player, hand, hitResult, state, pos) -> {
        for (var event : callbacks) {
            var newResult = event.onUseBlockPost(level, player, hand, hitResult, state, pos);
            if (newResult != InteractionResult.PASS) return newResult;
        }
        return InteractionResult.PASS;
    });

    public static final Event<EntityPost> ENTITY_POST = EventFactory.createArrayBacked(EntityPost.class, callbacks -> (level, player, hand, hitResult, entity) -> {
        for (var event : callbacks) {
            var newResult = event.onUseEntityPost(level, player, hand, hitResult, entity);
            if (newResult != InteractionResult.PASS) return newResult;
        }
        return InteractionResult.PASS;
    });

    @FunctionalInterface
    public interface ItemPre {
        /**
         * Called before an item is used.
         *
         * @param level  the level the player is in
         * @param player the player using the item
         * @param hand   the hand holding the item
         *
         * @return {@link InteractionResultHolder#pass(Object)} to let the item behave normally, or any other result to override it
         */
        InteractionResultHolder<ItemStack> onUseItemPre(Level level, Player player, InteractionHand hand);
    }

    @FunctionalInterface
    public interface ItemPost {
        InteractionResult onUseItemPost(Level level, Player player, InteractionHand hand, ItemStack stack);
    }

    @FunctionalInterface
    public interface BlockPost {
        InteractionResult onUseBlockPost(Level level, Player player, InteractionHand hand, BlockHitResult hitResult, BlockState state, BlockPos pos);
    }

    @FunctionalInterface
    public interface EntityPost {
        InteractionResult onUseEntityPost(Level level, Player player, InteractionHand hand, EntityHitResult hitResult, Entity entity);
    }
}