package net.justmili.corelibs.libs.api.events.server;

import net.justmili.corelibs.libs.api.events.Event;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.CloseableResourceManager;

public final class ServerLifecycleEvents {
    private ServerLifecycleEvents() {
    }

    public static final Event<Starting> STARTING = Event.create(Starting.class, callbacks -> server -> {
        for (var event : callbacks) event.onServerStarting(server);
    });
    public static final Event<Stopping> STOPPING = Event.create(Stopping.class, callbacks -> server -> {
        for (var event : callbacks) event.onServerStopping(server);
    });
    public static final Event<Stopped> STOPPED = Event.create(Stopped.class, callbacks -> server -> {
        for (var event : callbacks) event.onServerStopped(server);
    });

    public static final Event<SavePre> SAVE_PRE = Event.create(SavePre.class, callbacks -> server -> {
        for (var event : callbacks) event.onSavePre(server);
    });
    public static final Event<SavePost> SAVE_POST = Event.create(SavePost.class, callbacks -> server -> {
        for (var event : callbacks) event.onSavePost(server);
    });

    public static final Event<DatapackReloadPre> DATAPACK_RELOAD_PRE = Event.create(DatapackReloadPre.class, callbacks -> (server, resourceManager) -> {
        for (var event : callbacks) event.onDatapackReloadPre(server, resourceManager);
    });
    public static final Event<DatapackReloadPost> DATAPACK_RELOAD_POST = Event.create(DatapackReloadPost.class, callbacks -> (server, resourceManager, success) -> {
        for (var event : callbacks) event.onDatapackReloadPost(server, resourceManager, success);
    });
    public static final Event<DatapackSyncContents> DATAPACK_SYNC_CONTENTS = Event.create(DatapackSyncContents.class, callbacks -> (player, joined) -> {
        for (var event : callbacks) event.onDatapackSyncContents(player, joined);
    });

    @FunctionalInterface
    public interface Starting {
        void onServerStarting(MinecraftServer server);
    }

    @FunctionalInterface
    public interface Stopping {
        void onServerStopping(MinecraftServer server);
    }

    @FunctionalInterface
    public interface Stopped {
        void onServerStopped(MinecraftServer server);
    }

    @FunctionalInterface
    public interface SavePre {
        void onSavePre(MinecraftServer server);
    }

    @FunctionalInterface
    public interface SavePost {
        void onSavePost(MinecraftServer server);
    }

    @FunctionalInterface
    public interface DatapackReloadPre {
        void onDatapackReloadPre(MinecraftServer server, CloseableResourceManager resourceManager);
    }

    @FunctionalInterface
    public interface DatapackReloadPost {
        void onDatapackReloadPost(MinecraftServer server, CloseableResourceManager resourceManager, boolean success);
    }

    @FunctionalInterface
    public interface DatapackSyncContents {
        void onDatapackSyncContents(ServerPlayer player, boolean joined);
    }
}