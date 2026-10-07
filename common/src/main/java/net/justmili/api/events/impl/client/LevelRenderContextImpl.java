package net.justmili.api.events.impl.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.justmili.api.rendering.LevelRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public final class LevelRenderContextImpl implements LevelRenderContext, LevelRenderContext.BlockOutlineContext {
    private LevelRenderer levelRenderer;
    private PoseStack pose;
    private float partialTick;
    private long limitTime;
    private boolean blockOutlines;
    private Camera camera;
    private GameRenderer gameRenderer;
    private LightTexture lightTexture;
    private Matrix4f projectionMatrix;
    private ClientLevel level;
    private ProfilerFiller profiler;
    private boolean advancedTranslucency;
    private MultiBufferSource buffers;
    private Frustum frustum;

    private Entity entity;
    private double cameraX;
    private double cameraY;
    private double cameraZ;
    private BlockPos pos;
    private BlockState blockState;

    public void prepare(LevelRenderer levelRenderer, PoseStack pose, float partialTick, long limitTime, boolean blockOutlines, Camera camera, GameRenderer gameRenderer,
                        LightTexture lightTexture, Matrix4f projectionMatrix, MultiBufferSource buffers, ProfilerFiller profiler, boolean advancedTranslucency, ClientLevel level) {
        this.levelRenderer = levelRenderer;
        this.pose = pose;
        this.partialTick = partialTick;
        this.limitTime = limitTime;
        this.blockOutlines = blockOutlines;
        this.camera = camera;
        this.gameRenderer = gameRenderer;
        this.lightTexture = lightTexture;
        this.projectionMatrix = projectionMatrix;
        this.buffers = buffers;
        this.profiler = profiler;
        this.advancedTranslucency = advancedTranslucency;
        this.level = level;
        this.frustum = null;
    }

    public void setFrustum(Frustum frustum) {
        this.frustum = frustum;
    }

    public void setBlockOutlines(boolean value) {
        this.blockOutlines = value;
    }

    public void prepareBlockOutline(Entity entity, double x, double y, double z, BlockPos pos, BlockState state) {
        this.entity = entity;
        this.cameraX = x;
        this.cameraY = y;
        this.cameraZ = z;
        this.pos = pos;
        this.blockState = state;
    }

    @Override
    public LevelRenderer levelRenderer() {
        return levelRenderer;
    }

    @Override
    public PoseStack poseStack() {
        return pose;
    }

    @Override
    public float tickCounter() {
        return partialTick;
    }

    @Override
    public long limitTime() {
        return limitTime;
    }

    @Override
    public boolean blockOutlines() {
        return blockOutlines;
    }

    @Override
    public Camera camera() {
        return camera;
    }

    @Override
    public GameRenderer gameRenderer() {
        return gameRenderer;
    }

    @Override
    public LightTexture lightmapTexture() {
        return lightTexture;
    }

    @Override
    public Matrix4f projectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public ClientLevel level() {
        return level;
    }

    @Override
    public ProfilerFiller profiler() {
        return profiler;
    }

    @Override
    public boolean advancedTranslucency() {
        return advancedTranslucency;
    }

    @Override
    public MultiBufferSource buffers() {
        return buffers;
    }

    @Override
    public Frustum frustum() {
        return frustum;
    }

    @Override
    public Entity entity() {
        return entity;
    }

    @Override
    public double cameraX() {
        return cameraX;
    }

    @Override
    public double cameraY() {
        return cameraY;
    }

    @Override
    public double cameraZ() {
        return cameraZ;
    }

    @Override
    public BlockPos position() {
        return pos;
    }

    @Override
    public BlockState blockState() {
        return blockState;
    }
}