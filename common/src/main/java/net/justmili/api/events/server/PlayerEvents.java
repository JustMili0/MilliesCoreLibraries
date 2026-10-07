package net.justmili.api.events.server;

import net.justmili.api.events.base.Event;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerEvents {
    private PlayerEvents() {
    }

    public static final Event<Respawn> RESPAWN = Event.create(Respawn.class, callbacks -> (oldPlayer, newPlayer, alive) -> {
        for (var event : callbacks) event.onRespawn(oldPlayer, newPlayer, alive);
    });
    public static final Event<Clone> CLONE = Event.create(Clone.class, callbacks -> (oldPlayer, newPlayer, alive) -> {
        for (var event : callbacks) event.onClone(oldPlayer, newPlayer, alive);
    });

    public static final Event<CraftItem> CRAFT_ITEM = Event.create(CraftItem.class, callbacks -> (player, itemCrafted, container) -> {
        for (var event : callbacks) event.onCraftItem(player, itemCrafted, container);
    });
    public static final Event<SmeltItem> SMELT_ITEM = Event.create(SmeltItem.class, callbacks -> (player, itemSmelted) -> {
        for (var event : callbacks) event.onSmeltItem(player, itemSmelted);
    });
    public static final Event<DropItem> DROP_ITEM = Event.create(DropItem.class, callbacks -> (player, itemDropped) -> {
        for (var event : callbacks) {
            if (!event.onDropItem(player, itemDropped)) return false;
        }

        return true;
    });
    public static final Event<CanPickupItem> CAN_PICKUP_ITEM = Event.create(CanPickupItem.class, callbacks -> (player, itemEntity, stack) -> {
        for (var event : callbacks) {
            if (!event.canPickupItem(player, itemEntity, stack)) return false;
        }

        return true;
    });
    public static final Event<PickupItemPre> PICKUP_ITEM_PRE = Event.create(PickupItemPre.class, callbacks -> (player, itemEntity, stack) -> {
        for (var event : callbacks) {
            var result = event.onStartPickupItem(player, itemEntity, stack);
            if (!result) return false;
        } // TODO: hook up

        return true;
    });
    public static final Event<PickupItemPost> PICKUP_ITEM_POST = Event.create(PickupItemPost.class, callbacks -> (player, itemEntity, stack) -> {
        for (var event : callbacks) event.onEndPickupItem(player, itemEntity, stack);
    }); // TODO: hook up

    public static final Event<ChangeDimension> CHANGED_DIMENSION = Event.create(ChangeDimension.class, callbacks -> (player, fromLevel, toLevel) -> {
        for (var event : callbacks) event.onChangedDimension(player, fromLevel, toLevel);
    }); // TODO: hook up

    public static final Event<OpenMenu> OPEN_MENU = Event.create(OpenMenu.class, callbacks -> (player, menu) -> {
        for (var event : callbacks) event.onOpenMenu(player, menu);
    }); // TODO: hook up
    public static final Event<CloseMenu> CLOSE_MENU = Event.create(CloseMenu.class, callbacks -> (player, menu) -> {
        for (var event : callbacks) event.onCloseMenu(player, menu);
    }); // TODO: hook up

    public static final Event<FillBucket> FILL_BUCKET = Event.create(FillBucket.class, callbacks -> (player, bucket, hitResult) -> {
        for (var event : callbacks) {
            var result = event.onFillBucket(player, bucket, hitResult);
            if (result != InteractionResult.PASS) return result; // CORELIBS: Should it default to PASS??
        }

        return InteractionResult.PASS;
    }); // TODO: hook up

    public static final Event<AttackEntity> ATTACK_ENTITY = Event.create(AttackEntity.class, callbacks -> (player, level, target, hand, hitResult) -> {
        for (var event : callbacks) {
            var result = event.onAttackedEntity(player, level, target, hand, hitResult);
            if (result != InteractionResult.PASS) return result;
        }

        return InteractionResult.PASS;
    });

    @FunctionalInterface
    public interface Respawn {
        void onRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive);
    }

    @FunctionalInterface
    public interface Clone {
        void onClone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive);
    }

    @FunctionalInterface
    public interface CraftItem {
        void onCraftItem(Player player, @NotNull ItemStack itemCrafted, Container container);
    }

    @FunctionalInterface
    public interface SmeltItem {
        void onSmeltItem(Player player, @NotNull ItemStack itemSmelted);
    }

    @FunctionalInterface
    public interface DropItem {
        boolean onDropItem(ServerPlayer player, ItemEntity item);
    }

    @FunctionalInterface
    public interface CanPickupItem {
        boolean canPickupItem(Player player, ItemEntity item, ItemStack stack);
    }

    @FunctionalInterface
    public interface PickupItemPre {
        boolean onStartPickupItem(Player player, ItemEntity item, ItemStack stack);
    }

    @FunctionalInterface
    public interface PickupItemPost {
        void onEndPickupItem(Player player, ItemEntity item, ItemStack stack);
    }

    @FunctionalInterface
    public interface ChangeDimension {
        void onChangedDimension(ServerPlayer player, ResourceKey<Level> fromLevel, ResourceKey<Level> toLevel);
    }

    @FunctionalInterface
    public interface OpenMenu {
        void onOpenMenu(Player player, AbstractContainerMenu menu);
    }

    @FunctionalInterface
    public interface CloseMenu {
        void onCloseMenu(Player player, AbstractContainerMenu menu);
    }

    @FunctionalInterface
    public interface FillBucket {
        InteractionResult onFillBucket(Player player, ItemStack bucket, BlockHitResult hitResult);
    }

    @FunctionalInterface
    public interface AttackEntity {
        InteractionResult onAttackedEntity(Player attacker, Level level, Entity target, InteractionHand hand, @Nullable EntityHitResult hitResult);
    }

    @FunctionalInterface
    public interface KillOtherEntityPost { // TODO: finish
        InteractionResult onEndKillOtherEntity();
    }
}