package net.justmili.corelibs.v1.api.events.bridge;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.justmili.corelibs.v1.api.events.server.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;

public final class ServerEventsBridge {

    public static void init() {

        // Server Ticks
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            TickEvents.SERVER_PRE.invoker().onStartTick(server); // Tick server
            for (var player : server.getPlayerList().getPlayers()) {
                TickEvents.PLAYER_PRE.invoker().onStartTick(player); // Tick players
            }
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            TickEvents.SERVER_POST.invoker().onEndTick(server); // Tick server
            for (var player : server.getPlayerList().getPlayers()) {
                TickEvents.PLAYER_POST.invoker().onEndTick(player); // Tick players
            }
        });
        ServerTickEvents.START_WORLD_TICK.register(level -> {
            TickEvents.LEVEL_PRE.invoker().onStartTick(level); // Tick level
            for (var entity : level.getAllEntities()) {
                TickEvents.ENTITY_PRE.invoker().onStartTick(entity); // Tick entities
            }
        });
        ServerTickEvents.END_WORLD_TICK.register(level -> {
            TickEvents.LEVEL_POST.invoker().onEndTick(level); // Tick level
            for (var entity : level.getAllEntities()) {
                TickEvents.ENTITY_POST.invoker().onEndTick(entity); // Tick entities
            }
        });

        // PlayerEvents
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
            PlayerEvents.RESPAWN.invoker().onRespawn(oldPlayer, newPlayer, alive));
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) ->
            PlayerEvents.CLONE.invoker().onClone(oldPlayer, newPlayer, alive));
        AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            if (level.isClientSide() || !(player instanceof ServerPlayer serverPlayer)) return InteractionResult.PASS;
            return PlayerEvents.ATTACK_ENTITY.invoker().onAttackedEntity(serverPlayer, level, entity, InteractionHand.MAIN_HAND, hitResult);
        });
        // TODO: finish hooking up PlayerEvents

        // PlayerAdvancementEvents
        // TODO: add

        // Use Events
        UseItemCallback.EVENT.register((player, level, hand) -> {
            var stack = player.getItemInHand(hand);
            UseEvents.ITEM_POST.invoker().onUseItemPost(level, player, hand, stack);
            return InteractionResultHolder.pass(stack);
        });
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            var pos = hitResult.getBlockPos();
            var state = level.getBlockState(pos);
            UseEvents.BLOCK_POST.invoker().onUseBlockPost(level, player, hand, pos, state, InteractionResult.PASS);
            return InteractionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
            UseEvents.ENTITY_POST.invoker().onUseEntityPost(level, player, hand, entity, InteractionResult.PASS);
            return InteractionResult.PASS;
        });
    }
}