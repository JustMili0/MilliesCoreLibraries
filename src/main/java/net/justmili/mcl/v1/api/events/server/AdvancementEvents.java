package net.justmili.mcl.v1.api.events.server;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.*;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class AdvancementEvents {

    public static final Event<Reload> RELOAD = EventFactory.createArrayBacked(Reload.class, callbacks -> (player, advancementManager) -> {
        for (var event : callbacks) {
            event.onReload(player, advancementManager);
        }
    });

    public static final Event<StartProgress> START_PROGRESS = EventFactory.createArrayBacked(StartProgress.class, callbacks -> (player, advancement, progress) -> {
        for (var event : callbacks) {
            event.onStartProgress(player, advancement, progress);
        }
    });

    public static final Event<UpdateProgress> UPDATE_PROGRESS = EventFactory.createArrayBacked(UpdateProgress.class, callbacks -> (progress, criteria, requirements) -> {
        for (var event : callbacks) {
            event.onUpdateProgress(progress, criteria, requirements);
        }
    });

    public static final Event<Award> AWARD = EventFactory.createArrayBacked(Award.class, callbacks -> (player, advancement) -> {
        for (var event : callbacks) {
            event.onAward(player, advancement);
        }
    });

    public static final Event<Revoke> REVOKE = EventFactory.createArrayBacked(Revoke.class, callbacks -> (player, advancement) -> {
        for (var event : callbacks) {
            event.onRevoke(player, advancement);
        }
    });

    @FunctionalInterface
    public interface Reload {
        void onReload(ServerPlayer player, ServerAdvancementManager advancementManager);
    }

    @FunctionalInterface
    public interface StartProgress {
        void onStartProgress(ServerPlayer player, Advancement advancement, AdvancementProgress progress);
    }

    @FunctionalInterface
    public interface UpdateProgress {
        void onUpdateProgress(AdvancementProgress progress, Map<String, Criterion> criteria, String[][] requirements);
    }

    @FunctionalInterface
    public interface Award {
        void onAward(ServerPlayer player, Advancement advancement);
    }

    @FunctionalInterface
    public interface Revoke {
        void onRevoke(ServerPlayer player, Advancement advancement);
    }
}