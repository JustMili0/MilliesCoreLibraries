package net.justmili.corelibs.api.events.client;

import net.justmili.corelibs.api.events.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public class ClientTickEvents {
    private ClientTickEvents() {
    }

    public static final Event<ClientPre> CLIENT_PRE = Event.create(ClientPre.class, callbacks -> minecraft -> {
        for (var event : callbacks) event.onStartTick(minecraft);
    });
    public static final Event<ClientPost> CLIENT_POST = Event.create(ClientPost.class, callbacks -> minecraft -> {
        for (var event : callbacks) event.onEndTick(minecraft);
    });

    public static final Event<LevelPre> LEVEL_PRE = Event.create(LevelPre.class, callbacks -> level -> {
        for (var event : callbacks) event.onStartTick(level);
    });
    public static final Event<LevelPost> LEVEL_POST = Event.create(LevelPost.class, callbacks -> level -> {
        for (var event : callbacks) event.onEndTick(level);
    });

    @FunctionalInterface
    public interface ClientPre {
        void onStartTick(Minecraft minecraft);
    }

    @FunctionalInterface
    public interface ClientPost {
        void onEndTick(Minecraft minecraft);
    }

    @FunctionalInterface
    public interface LevelPre {
        void onStartTick(ClientLevel level);
    }

    @FunctionalInterface
    public interface LevelPost {
        void onEndTick(ClientLevel level);
    }
}
