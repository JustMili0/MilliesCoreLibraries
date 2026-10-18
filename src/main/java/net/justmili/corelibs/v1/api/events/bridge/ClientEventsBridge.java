package net.justmili.corelibs.v1.api.events.bridge;

import net.justmili.corelibs.v1.api.events.client.BlockOutlineRenderEvent;

//? if >= 26.2 {
/*import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
 *///?} else if >= 26.1 {
/*import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
 *///?} else if >= 1.21.11 {
/*import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
*///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
 //?}

public class ClientEventsBridge {

    public static void init() {
        //? if >=1.21.11 {
        /*/^? if >=26.1 {^//^LevelRenderEvents^//^?} else {^/WorldRenderEvents/^?}^/.BEFORE_BLOCK_OUTLINE.register((context, outline) -> BlockOutlineRenderEvent.RENDER_PRE.invoker().onStartRender(context, outline));
        /^? if >=26.2 {^//^LevelExtractionEvents^//^?} else if >=26.1 {^//^LevelRenderEvents^//^?} else {^/WorldRenderEvents/^?}^/.AFTER_BLOCK_OUTLINE_EXTRACTION.register((context, hitResult) -> BlockOutlineRenderEvent.EXTRACTION_POST.invoker().onEndExtraction(context, hitResult));
        *///?} else {
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, hitResult) -> BlockOutlineRenderEvent.RENDER_PRE.invoker().onStartRender(context, hitResult));
        WorldRenderEvents.BLOCK_OUTLINE.register((context, outline) -> BlockOutlineRenderEvent.RENDER_POST.invoker().onEndRender(context, outline));
        //?}
    }
}