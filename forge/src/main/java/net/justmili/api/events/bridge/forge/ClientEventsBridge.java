package net.justmili.api.events.bridge.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

import static net.justmili.api.events.client.ClientLifecycleEvents.STARTED;
import static net.justmili.api.events.client.ClientLifecycleEvents.STOPPING;
import static net.justmili.api.events.client.ClientTickEvents.*;

public class ClientEventsBridge {
    private ClientEventsBridge() {
    }

    public static void init() {
        var client = Minecraft.getInstance();

        // Client Lifecycle
        STARTED.invoker().onClientStarted(client);
        MinecraftForge.EVENT_BUS.addListener((GameShuttingDownEvent event) -> {
            if (Thread.currentThread().getThreadGroup() != SidedThreadGroups.CLIENT) return;
            STOPPING.invoker().onClientStopping(client);
        });

        // Connection
        // TODO: add

        // Client Ticks
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (isPre(event)) CLIENT_PRE.invoker().onStartTick(client);
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (isPost(event)) CLIENT_POST.invoker().onEndTick(client);
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.LevelTickEvent event) -> {
            if (!isPre(event) || !isClientSide(event)) return;
            if (!(event.level instanceof ClientLevel level)) return;

            LEVEL_PRE.invoker().onStartTick(level);
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.LevelTickEvent event) -> {
            if (!isPost(event) || !isClientSide(event)) return;
            if (!(event.level instanceof ClientLevel level)) return;

            LEVEL_POST.invoker().onEndTick(level);
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