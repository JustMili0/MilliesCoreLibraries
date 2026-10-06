package net.justmili.api.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import javax.swing.text.html.parser.Entity;

public interface LevelRenderContext {

    LevelRenderer levelRenderer();

    @Nullable PoseStack poseStack();

    float tickCounter();

    long limitTime();

    boolean blockOutlines();

    Camera camera();

    GameRenderer gameRenderer();

    LightTexture lightmapTexture();

    Matrix4f projectionMatrix();

    ClientLevel level();

    ProfilerFiller profiler();

    boolean advancedTranslucency();

    @Nullable MultiBufferSource buffers();

    @Nullable Frustum frustum();

    interface BlockOutlineContext {

        Entity entity();

        double cameraX();

        double cameraY();

        double cameraZ();

        BlockPos position();

        BlockState state();
    }
}
