package net.justmili.corelibs.libs.util.client;

import net.justmili.corelibs.libs.api.rendering.LevelRenderContext;

public record BlockGroupOutlineContext(LevelRenderContext context, LevelRenderContext.BlockOutlineContext outline) {
}