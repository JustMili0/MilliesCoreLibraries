package net.justmili.corelibs.libs.api.events.server;

import net.justmili.corelibs.libs.api.events.Event;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ServerTickEvents {
    private ServerTickEvents() {
    }

    public static final Event<ServerPre> SERVER_PRE = Event.create(ServerPre.class, callbacks -> server -> {
        for (var event : callbacks) event.onStartTick(server);
    });
    public static final Event<ServerPost> SERVER_POST = Event.create(ServerPost.class, callbacks -> server -> {
        for (var event : callbacks) event.onEndTick(server);
    });

    public static final Event<LevelPre> LEVEL_PRE = Event.create(LevelPre.class, callbacks -> level -> {
        for (var event : callbacks) event.onStartTick(level);
    });
    public static final Event<LevelPost> LEVEL_POST = Event.create(LevelPost.class, callbacks -> level -> {
        for (var event : callbacks) event.onEndTick(level);
    });

    public static final Event<PlayerPre> PLAYER_PRE = Event.create(PlayerPre.class, callbacks -> player -> {
        for (var event : callbacks) event.onStartTick(player);
    });
    public static final Event<PlayerPost> PLAYER_POST = Event.create(PlayerPost.class, callbacks -> player -> {
        for (var event : callbacks) event.onEndTick(player);
    });
    public static final Event<EntityPre> ENTITY_PRE = Event.create(EntityPre.class, callbacks -> entity -> {
        for (var event : callbacks) event.onStartTick(entity);
    });
    public static final Event<EntityPost> ENTITY_POST = Event.create(EntityPost.class, callbacks -> entity -> {
        for (var event : callbacks) event.onEndTick(entity);
    });

    @FunctionalInterface
    public interface ServerPre {
        void onStartTick(MinecraftServer server);
    }

    @FunctionalInterface
    public interface ServerPost {
        void onEndTick(MinecraftServer server);
    }

    @FunctionalInterface
    public interface LevelPre {
        void onStartTick(ServerLevel level);
    }

    @FunctionalInterface
    public interface LevelPost {
        void onEndTick(ServerLevel level);
    }

    @FunctionalInterface
    public interface PlayerPre {
        void onStartTick(Player player);
    }

    @FunctionalInterface
    public interface PlayerPost {
        void onEndTick(Player player);
    }

    @FunctionalInterface
    public interface EntityPre {
        void onStartTick(Entity entity);
    }

    @FunctionalInterface
    public interface EntityPost {
        void onEndTick(Entity entity);
    }
}
