package net.justmili.mcl.v1.api.events.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

//? if >= 26.1 {
/*import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
*///?} else if >= 1.21.11 {
/*import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
*///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
 //?}

public class BlockOutlineRenderEvent {

    public static final Event<RenderPre> RENDER_PRE = EventFactory.createArrayBacked(RenderPre.class, (context, hitResult) -> true, callbacks -> (context, hitResult) -> {
        boolean shouldRender = true;
        for (var event : callbacks) {
            if (!event.onStartRender(context, hitResult)) shouldRender = false;
        }
        return shouldRender;
    });

    //? if >= 1.21.11 {
    /*public static final Event<ExtractionPost> EXTRACTION_POST = EventFactory.createArrayBacked(ExtractionPost.class, callbacks -> (context, hitResult) -> {
        for (var event : callbacks) event.onEndExtraction(context, hitResult);
    });

    @FunctionalInterface
    public interface ExtractionPost {
        void onEndExtraction(/^? if >= 26.1 {^//^LevelExtractionContext^//^?} else {^/WorldExtractionContext/^?}^/ context, @Nullable HitResult hitResult);
    }
    *///?} else {
    public static final Event<RenderPost> RENDER_POST = EventFactory.createArrayBacked(RenderPost.class, (context, outline) -> true, callbacks -> (context, outline) -> {
        boolean shouldRender = true;
        for (var event : callbacks) {
            if (!event.onEndRender(context, outline)) shouldRender = false;
        }
        return shouldRender;
    });

    @FunctionalInterface
    public interface RenderPost {
        boolean onEndRender(WorldRenderContext context, WorldRenderContext.BlockOutlineContext outline);
    }
    //?}

    @FunctionalInterface
    public interface RenderPre {
        boolean onStartRender(/*? if >= 26.1 {*//*LevelRenderContext*//*?} else {*/WorldRenderContext/*?}*/ context, /*? if >= 1.21.11 {*//*BlockOutlineRenderState outline*//*?} else {*/@Nullable HitResult hitResult/*?}*/);
    }
}