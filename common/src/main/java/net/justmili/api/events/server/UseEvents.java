package net.justmili.api.events.server;

import net.justmili.api.events.base.Event;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class UseEvents {
    private UseEvents() {
    }

    public static final Event<ItemPre> ITEM_PRE = Event.create(ItemPre.class, callbacks -> (level, player, hand) -> {
        for (var event : callbacks) {
            InteractionResultHolder<ItemStack> result = event.onUseItemPre(level, player, hand);
            if (result.getResult() != InteractionResult.PASS) return result;
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    });

    public static final Event<ItemPost> ITEM_POST = Event.create(ItemPost.class, callbacks -> (level, player, hand, stack, result) -> {
        for (var event : callbacks) event.onUseItemPost(level, player, hand, stack, result);
    });

    public static final Event<BlockPost> BLOCK_POST = Event.create(BlockPost.class, callbacks -> (level, player, hand, pos, state, result) -> {
        for (var event : callbacks) event.onUseBlockPost(level, player, hand, pos, state, result);
    });

    public static final Event<EntityPost> ENTITY_POST = Event.create(EntityPost.class, callbacks -> (level, player, hand, entity, result) -> {
        for (var event : callbacks) event.onUseEntityPost(level, player, hand, entity, result);
    });

    @FunctionalInterface
    public interface ItemPre {
        InteractionResultHolder<ItemStack> onUseItemPre(Level level, Player player, InteractionHand hand);
    }

    @FunctionalInterface
    public interface ItemPost {
        void onUseItemPost(Level level, Player player, InteractionHand hand, ItemStack stack, InteractionResult result);
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