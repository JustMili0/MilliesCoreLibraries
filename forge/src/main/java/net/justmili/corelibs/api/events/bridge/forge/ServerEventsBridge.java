package net.justmili.api.events.bridge.forge;

import net.justmili.corelibs.api.events.server.PlayerEvents;
import net.justmili.corelibs.api.events.server.UseEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;

public final class ServerEventsBridge {
    private ServerEventsBridge() { }

    public static void init() {
        // Server Lifecycle
        MinecraftForge.EVENT_BUS.addListener((ServerStartingEvent event) -> STARTING.invoker().onServerStarting(event.getServer()));
        MinecraftForge.EVENT_BUS.addListener((ServerStoppingEvent event) -> STOPPING.invoker().onServerStopping(event.getServer()));
        MinecraftForge.EVENT_BUS.addListener((ServerStoppedEvent event) -> STOPPED.invoker().onServerStopped(event.getServer()));
        MinecraftForge.EVENT_BUS.addListener((OnDatapackSyncEvent event) -> {
            var playerList = event.getPlayerList();
            var player = event.getPlayer();
            if (player != null) {
                DATAPACK_SYNC_CONTENTS.invoker().onDatapackSyncContents(player, true);
            } else {
                for (var serverPlayer : playerList.getPlayers()) {
                    DATAPACK_SYNC_CONTENTS.invoker().onDatapackSyncContents(serverPlayer, false);
                }
            }
        });

        // ServerConnectionEvents
        // TODO: add

        // Server Ticks
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent event) -> {
            if (isPre(event)) SERVER_PRE.invoker().onStartTick(event.getServer());
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent event) -> {
            if (isPost(event)) SERVER_POST.invoker().onEndTick(event.getServer());
        });

        MinecraftForge.EVENT_BUS.addListener((TickEvent.LevelTickEvent event) -> {
            if (!isPre(event) || isClientSide(event)) return;
            if (!(event.level instanceof ServerLevel level)) return;

            LEVEL_PRE.invoker().onStartTick(level); // Tick level
            for (var entity : level.getAllEntities()) ENTITY_PRE.invoker().onStartTick(entity); // Tick entities
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.LevelTickEvent event) -> {
            if (!isPost(event) || isClientSide(event)) return;
            if (!(event.level instanceof ServerLevel level)) return;

            LEVEL_POST.invoker().onEndTick(level); // Tick level
            for (var entity : level.getAllEntities()) ENTITY_POST.invoker().onEndTick(entity); // Tick entities
        });

        MinecraftForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent event) -> {
            if (isPre(event) && event.player instanceof ServerPlayer player) PLAYER_PRE.invoker().onStartTick(player);
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent event) -> {
            if (isPost(event) && event.player instanceof ServerPlayer player) PLAYER_POST.invoker().onEndTick(player);
        });

        // ServerLevelEvents
        // TODO: add

        // BlockEntityEvents
        // TODO: add

        // EntityEvents
        // TODO: add

        // LivingEntityEvents
        // TODO: add

        // PlayerEvents
        MinecraftForge.EVENT_BUS.addListener((AttackEntityEvent event) -> {
            var entity = event.getEntity();
            if (entity.level().isClientSide() || !(entity instanceof ServerPlayer player)) return;
            var result = PlayerEvents.ATTACK_ENTITY.invoker().onAttackedEntity(player, player.level(), event.getTarget(), InteractionHand.MAIN_HAND, null);
            if (result != InteractionResult.PASS) event.setCanceled(true);
        }); // TODO: change to mixin

        // PlayerAdvancementEvents
        // TODO: add

        // EntitySleepEvents
        // TODO: add

        // ChatEvents
        // TODO: add

        // UseEvents
        MinecraftForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickItem event) -> {
            var stack = event.getItemStack();
            UseEvents.ITEM_POST.invoker().onUseItemPost(event.getLevel(), event.getEntity(), event.getHand(), stack, InteractionResult.PASS);
        });
        MinecraftForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock event) -> {
            var pos = event.getPos();
            var state = event.getLevel().getBlockState(pos);
            UseEvents.BLOCK_POST.invoker().onUseBlockPost(event.getLevel(), event.getEntity(), event.getHand(), pos, state, InteractionResult.PASS);
        });
        MinecraftForge.EVENT_BUS.addListener((PlayerInteractEvent.EntityInteract event) -> {
            UseEvents.ENTITY_POST.invoker().onUseEntityPost(event.getLevel(), event.getEntity(), event.getHand(), event.getTarget(), InteractionResult.PASS);
        });
    }

    private static boolean isPre(TickEvent event) {
        return event.phase == TickEvent.Phase.START;
    }
    private static boolean isPost(TickEvent event) {
        return event.phase == TickEvent.Phase.END;
    }
    private static boolean isClientSide(TickEvent.LevelTickEvent event) {
        return event.level.isClientSide;
    }
}