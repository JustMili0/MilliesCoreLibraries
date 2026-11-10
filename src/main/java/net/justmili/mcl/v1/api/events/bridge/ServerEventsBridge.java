package net.justmili.mcl.v1.api.events.bridge;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.justmili.mcl.v1.api.events.server.*;
import net.minecraft.world.InteractionResultHolder;

public final class ServerEventsBridge {

    public static void init() {

        // Server Ticks
        ServerTickEvents.START_WORLD_TICK.register(level -> TickEvents.LEVEL_PRE.invoker().onStartTick(level)); // Tick level
        ServerTickEvents.END_WORLD_TICK.register(level -> TickEvents.LEVEL_POST.invoker().onEndTick(level)); // Tick level

        // PlayerEvents
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
            PlayerEvents.RESPAWN.invoker().onRespawn(oldPlayer, newPlayer, alive));
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) ->
            PlayerEvents.CLONE.invoker().onClone(oldPlayer, newPlayer, alive));
        AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) ->
            PlayerEvents.ATTACK_ENTITY.invoker().onAttackedEntity(player, level, entity, hand, hitResult));
        // TODO: finish hooking up PlayerEvents

        // Use Events
        UseItemCallback.EVENT.register((player, level, hand) -> {
            var stack = player.getItemInHand(hand);
            var result = UseEvents.ITEM_POST.invoker().onUseItemPost(level, player, hand, stack);
            return new InteractionResultHolder<>(result, stack);
        });
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            var pos = hitResult.getBlockPos();
            var state = level.getBlockState(pos);
            return UseEvents.BLOCK_POST.invoker().onUseBlockPost(level, player, hand, hitResult, state, pos);
        });
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            return UseEvents.ENTITY_POST.invoker().onUseEntityPost(level, player, hand, hitResult, entity);
        });
    }
}