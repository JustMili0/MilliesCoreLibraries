package net.justmili.mcl.v2.api.event.common;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.justmili.mcl.v2.lib.config.MConfigLoader;
import net.justmili.mcl.v2.lib.config.sync.MConfigSync;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Events related to Millie's Core Libraries' config library.
 */
public class MConfigEvents {

    public static final Event<Read> READ = EventFactory.createArrayBacked(Read.class, callbacks -> (loader, success) -> {
        for (var event : callbacks) {
            event.onRead(loader, success);
        }
    });

    public static final Event<Write> WRITE = EventFactory.createArrayBacked(Write.class, callbacks -> (loader, success) -> {
        for (var event : callbacks) {
            event.onWrite(loader, success);
        }
    });

    public static final Event<Load> LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> loader -> {
        for (var event : callbacks) {
            event.onLoad(loader);
        }
    });

    public static final Event<Reload> RELOAD = EventFactory.createArrayBacked(Reload.class, callbacks -> (loader, success) -> {
        for (var event : callbacks) {
            event.onReload(loader, success);
        }
    });

    public static final Event<Sync> SYNC_CLIENTBOUND = EventFactory.createArrayBacked(Sync.class, callbacks -> (synchronizer, player) -> {
        for (var event : callbacks) {
            event.onSync(synchronizer, player);
        }
    });

    public static final Event<SyncAll> SYNC_ALL_CLIENTBOUND = EventFactory.createArrayBacked(SyncAll.class, callbacks -> (synchronizer, server) -> {
        for (var event : callbacks) {
            event.onSync(synchronizer, server);
        }
    });

    @FunctionalInterface
    public interface Read {
        void onRead(MConfigLoader loader, boolean success);
    }

    @FunctionalInterface
    public interface Write {
        void onWrite(MConfigLoader loader, boolean success);
    }

    @FunctionalInterface
    public interface Load {
        void onLoad(MConfigLoader loader);
    }

    @FunctionalInterface
    public interface Reload {
        void onReload(MConfigLoader loader, boolean success);
    }

    @FunctionalInterface
    public interface Sync {
        void onSync(MConfigSync synchronizer, ServerPlayer player);
    }

    @FunctionalInterface
    public interface SyncAll {
        void onSync(MConfigSync synchronizer, MinecraftServer server);
    }
}