package net.justmili.corelibs.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.justmili.api.rendering.LevelRenderContext;
import net.justmili.util.utils.client.BlockGroupOutliner;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Nullable
    private ClientLevel level;

    @Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
    private void corelibs$onRenderHitOutline(PoseStack poseStack, VertexConsumer consumer, Entity entity, double camX, double camY, double camZ, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!(entity instanceof Player player) || this.level == null) return;

        LevelRenderContext context = new LevelRenderContext() {
            @Override
            public LevelRenderer levelRenderer() {
                return (LevelRenderer) (Object) this;
            }

            @Override
            public @Nullable PoseStack poseStack() {
                return poseStack;
            }

            @Override
            public float tickCounter() {
                return minecraft.getDeltaFrameTime();
            }

            @Override
            public long limitTime() {
                return 0L;
            }

            @Override
            public boolean blockOutlines() {
                return true;
            }

            @Override
            public Camera camera() {
                return minecraft.gameRenderer.getMainCamera();
            }

            @Override
            public GameRenderer gameRenderer() {
                return minecraft.gameRenderer;
            }

            @Override
            public LightTexture lightmapTexture() {
                return minecraft.gameRenderer.lightTexture();
            }

            @Override
            public Matrix4f projectionMatrix() {
                return new Matrix4f();
            }

            @Override
            public ClientLevel level() {
                return level;
            }

            @Override
            public ProfilerFiller profiler() {
                return minecraft.getProfiler();
            }

            @Override
            public boolean advancedTranslucency() {
                return false;
            }

            // Fetch modern buffer pipeline from render target context if needed
            @Override
            public @NotNull MultiBufferSource buffers() {
                return minecraft.renderBuffers().bufferSource();
            }

            @Override
            public @Nullable Frustum frustum() {
                return null;
            }
        };

        LevelRenderContext.BlockOutlineContext outline = new LevelRenderContext.BlockOutlineContext() {
            @Override
            public Entity entity() {
                return entity;
            }

            @Override
            public double cameraX() {
                return camX;
            }

            @Override
            public double cameraY() {
                return camY;
            }

            @Override
            public double cameraZ() {
                return camZ;
            }

            @Override
            public BlockPos position() {
                return pos;
            }

            @Override
            public BlockState state() {
                return state;
            }
        };

        boolean shouldRenderVanilla = BlockGroupOutliner.render(context, outline, player, matchedState -> matchedState.is(state.getBlock()), 10, 128, false);
        if (!shouldRenderVanilla) ci.cancel();
    }
}