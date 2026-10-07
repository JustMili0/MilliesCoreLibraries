package net.justmili.api.events.client;

import net.justmili.api.events.base.Event;
import net.justmili.api.rendering.LevelRenderContext;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class LevelRenderEvents {
    private LevelRenderEvents() {
    }

    public static final Event<Start> START = Event.create(Start.class, callbacks -> context -> {
        for (var event : callbacks) {
            event.onStart(context);
        }
    });

    public static final Event<SetupPost> SETUP_POST = Event.create(SetupPost.class, callbacks -> context -> {
        for (var event : callbacks) {
            event.onEndSetup(context);
        }
    });

    public static final Event<EntitiesPre> ENTITIES_PRE = Event.create(EntitiesPre.class, callbacks -> context -> {
        for (var event : callbacks) {
            event.onStartEntities(context);
        }
    });
    public static final Event<EntitiesPost> ENTITIES_POST = Event.create(EntitiesPost.class, callbacks -> context -> {
        for (var event : callbacks) {
            event.onEndEntities(context);
        }
    });

    public static final Event<BlockOutlinePre> BLOCK_OUTLINE_PRE = Event.create(BlockOutlinePre.class, callbacks -> (context, hitResult) -> {
        boolean shouldRender = true;
        for (var event : callbacks) {
            if (!event.onStartOutline(context, hitResult)) shouldRender = false;
        }
        return shouldRender;
    });
    public static final Event<BlockOutlinePost> BLOCK_OUTLINE_POST = Event.create(BlockOutlinePost.class, callbacks -> (context, outline) -> {
        boolean shouldRender = true;
        for (var event : callbacks) {
            if (!event.onEndOutline(context, outline)) shouldRender = false;
        }
        return shouldRender;
    });

    public static final Event<DebugRenderPre> DEBUG_RENDER_PRE = Event.create(DebugRenderPre.class, callbacks -> context -> {
        for (var event : callbacks) event.onStartDebugRender(context);
    });

    public static final Event<TranslucentPost> TRANSLUCENT_POST = Event.create(TranslucentPost.class, callbacks -> context -> {
        for (var event : callbacks) event.onEndTranslucent(context);
    });

    public static final Event<Last> LAST = Event.create(Last.class, callbacks -> context -> {
        for (var event : callbacks) event.onLast(context);
    });

    public static final Event<End> END = Event.create(End.class, callbacks -> context -> {
        for (var event : callbacks) event.onEnd(context);
    });

    @FunctionalInterface
    public interface Start {
        void onStart(LevelRenderContext context);
    }

    @FunctionalInterface
    public interface SetupPost {
        void onEndSetup(LevelRenderContext context);
    }

    @FunctionalInterface
    public interface EntitiesPre {
        void onStartEntities(LevelRenderContext context);
    }

    @FunctionalInterface
    public interface EntitiesPost {
        void onEndEntities(LevelRenderContext context);
    }

    @FunctionalInterface
    public interface BlockOutlinePre {
        boolean onStartOutline(LevelRenderContext context, @Nullable HitResult hitResult);
    }

    @FunctionalInterface
    public interface BlockOutlinePost {
        boolean onEndOutline(LevelRenderContext context, LevelRenderContext.BlockOutlineContext outline);
    }

    @FunctionalInterface
    public interface DebugRenderPre {
        void onStartDebugRender(LevelRenderContext context);
    }

    @FunctionalInterface
    public interface TranslucentPost {
        void onEndTranslucent(LevelRenderContext context);
    }

    @FunctionalInterface
    public interface Last {
        void onLast(LevelRenderContext context);
    }

    @FunctionalInterface
    public interface End {
        void onEnd(LevelRenderContext context);
    }
}
