package net.justmili.corelibs.util.client;

import net.justmili.corelibs.api.rendering.LevelRenderContext;

public record BlockGroupOutlineContext(LevelRenderContext context, LevelRenderContext.BlockOutlineContext outline) {
}