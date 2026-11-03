package net.justmili.mcl.v1.api.events.server;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;

public class PlayerAdvancementEvents {
    public static final Event<StartProgress> START_PROGRESS = EventFactory.createArrayBacked(StartProgress.class, callbacks -> (player, advancement, advancementProgress) -> {
        for (var event : callbacks) {
            event.startProgress(player, advancement, advancementProgress);
        }
    });

    public static final Event<Award> AWARD = EventFactory.createArrayBacked(Award.class, callbacks -> (player, advancement) -> {
        for (var event : callbacks) {
            event.awardAdvancement(player, advancement);
        }
    });

    public static final Event<Revoke> REVOKE = EventFactory.createArrayBacked(Revoke.class, callbacks -> (player, advancement) -> {
        for (var event : callbacks) {
            event.revokeAdvancement(player, advancement);
        }
    });

    @FunctionalInterface
    public interface StartProgress {
        void startProgress(ServerPlayer player, Advancement advancement, AdvancementProgress advancementProgress);
    }

    @FunctionalInterface
    public interface Award {
        void awardAdvancement(ServerPlayer player, Advancement advancement);
    }

    @FunctionalInterface
    public interface Revoke {
        void revokeAdvancement(ServerPlayer player, Advancement advancement);
    }

}
