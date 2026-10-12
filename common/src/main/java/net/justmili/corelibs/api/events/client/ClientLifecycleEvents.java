package net.justmili.corelibs.api.events.client;

import net.justmili.corelibs.api.events.Event;
import net.minecraft.client.Minecraft;

public class ClientLifecycleEvents {
    private ClientLifecycleEvents() {
    }

    public static final Event<Started> STARTED = Event.create(Started.class, callbacks -> minecraft -> {
        for (var event : callbacks) event.onClientStarted(minecraft);
    });
    public static final Event<Stopping> STOPPING = Event.create(Stopping.class, callbacks -> minecraft -> {
        for (var event : callbacks) event.onClientStopping(minecraft);
    });

    @FunctionalInterface
    public interface Started {
        void onClientStarted(Minecraft minecraft);
    }

    @FunctionalInterface
    public interface Stopping {
        void onClientStopping(Minecraft minecraft);
    }
}
