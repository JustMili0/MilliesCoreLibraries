package net.justmili.corelibs.libs.api.events.bridge.fabric;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import static net.justmili.corelibs.libs.api.events.client.ClientLifecycleEvents.STARTED;
import static net.justmili.corelibs.libs.api.events.client.ClientLifecycleEvents.STOPPING;
import static net.justmili.corelibs.libs.api.events.client.ClientTickEvents.*;

public final class ClientEventsBridge {
    private ClientEventsBridge() {}

    public static void init() {
        // Client Lifecycle
        ClientLifecycleEvents.CLIENT_STARTED.register(minecraft -> STARTED.invoker().onClientStarted(minecraft));
        ClientLifecycleEvents.CLIENT_STOPPING.register(minecraft -> STOPPING.invoker().onClientStopping(minecraft));

        // Connection
        // TODO: add

        // Client Ticks
        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> CLIENT_PRE.invoker().onStartTick(minecraft));
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> CLIENT_POST.invoker().onEndTick(minecraft));
        ClientTickEvents.START_WORLD_TICK.register(level -> LEVEL_PRE.invoker().onStartTick(level));
        ClientTickEvents.END_WORLD_TICK.register(level -> LEVEL_POST.invoker().onEndTick(level));
    }
}