package net.justmili.corelibs.libs.api.events.server;

import net.justmili.corelibs.libs.api.events.Event;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;

public class ServerLevelEvents {
    private ServerLevelEvents() {
    }

    public static final Event<LevelLoad> LEVEL_LOAD = Event.create(LevelLoad.class, callbacks -> (server, level) -> {
        for (var event : callbacks) event.onLevelLoad(server, level);
    });
    public static final Event<LevelUnload> LEVEL_UNLOAD = Event.create(LevelUnload.class, callbacks -> (server, level) -> {
        for (var event : callbacks) event.onLevelUnload(server, level);
    });

    public static final Event<ChunkLoad> CHUNK_LOAD = Event.create(ChunkLoad.class, callbacks -> (level, chunk) -> {
        for (var event : callbacks) event.onChunkLoad(level, chunk);
    });
    public static final Event<ChunkUnload> CHUNK_UNLOAD = Event.create(ChunkUnload.class, callbacks -> (level, chunk) -> {
        for (var event : callbacks) event.onChunkUnload(level, chunk);
    });

    @FunctionalInterface
    public interface LevelLoad {
        void onLevelLoad(MinecraftServer server, ServerLevel level);
    }

    @FunctionalInterface
    public interface LevelUnload {
        void onLevelUnload(MinecraftServer server, ServerLevel level);
    }

    @FunctionalInterface
    public interface ChunkLoad {
        void onChunkLoad(ServerLevel level, LevelChunk chunk);
    }

    @FunctionalInterface
    public interface ChunkUnload {
        void onChunkUnload(ServerLevel level, LevelChunk chunk);
    }
}