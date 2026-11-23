package net.justmili.mcl.v2.api.events.server;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.justmili.mcl.v2.lib.config.MConfigLoader;
import net.justmili.mcl.v2.lib.config.sync.MConfigSync;
import net.minecraft.server.MinecraftServer;

/**
 * Events related to Millie's Core Libraries' config library on the server and common side.
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

    public static final Event<Sync> SYNC_CLIENTBOUND = EventFactory.createArrayBacked(Sync.class, callbacks -> (synchronizer, server) -> {
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
        void onSync(MConfigSync synchronizer, MinecraftServer server);
    }
}
