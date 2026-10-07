package net.justmili.corelibs.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.justmili.api.events.client.LevelRenderEvents;
import net.justmili.api.events.impl.client.LevelRenderContextImpl;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @Shadow
    private ClientLevel level;

    @Shadow
    private PostChain transparencyChain;

    @Unique
    private final LevelRenderContextImpl corelibs$context = new LevelRenderContextImpl();

    @Unique
    private boolean corelibs$didRenderParticles;

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void corelibs$renderStart(PoseStack pose, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera,
                                    GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        corelibs$context.prepare((LevelRenderer) (Object) this, pose, partialTick, finishNanoTime, renderBlockOutline, camera,
            gameRenderer, lightTexture, projectionMatrix, renderBuffers.bufferSource(), level.getProfiler(), transparencyChain != null, level);
        LevelRenderEvents.START.invoker().onStart(corelibs$context);
        corelibs$didRenderParticles = false;
    }

    @Inject(method = "setupRender", at = @At("RETURN"))
    private void corelibs$setupPost(Camera camera, Frustum frustum, boolean hasCapturedFrustum, boolean isSpectator, CallbackInfo ci) {
        corelibs$context.setFrustum(frustum);
        LevelRenderEvents.SETUP_POST.invoker().onEndSetup(corelibs$context);
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderChunkLayer(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/PoseStack;DDDLorg/joml/Matrix4f;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void corelibs$entitiesPre(CallbackInfo ci) {
        LevelRenderEvents.ENTITIES_PRE.invoker().onStartEntities(corelibs$context);
    }

    @Inject(method = "renderLevel", at = @At(value = "CONSTANT", args = "stringValue=blockentities", ordinal = 0))
    private void corelibs$entitiesPost(CallbackInfo ci) {
        LevelRenderEvents.ENTITIES_POST.invoker().onEndEntities(corelibs$context);
    }

    @Inject(method = "renderLevel", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;hitResult:Lnet/minecraft/world/phys/HitResult;", shift = At.Shift.AFTER, ordinal = 1))
    private void corelibs$outlinePre(CallbackInfo ci) {
        corelibs$context.setBlockOutlines(LevelRenderEvents.BLOCK_OUTLINE_PRE.invoker().onStartOutline(corelibs$context, minecraft.hitResult));
    }

    @Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
    private void corelibs$outlinePost(PoseStack poseStack, VertexConsumer consumer, Entity entity, double camX, double camY, double camZ, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!corelibs$context.blockOutlines()) {
            ci.cancel();
            return;
        }
        corelibs$context.prepareBlockOutline(entity, camX, camY, camZ, pos, state);
        if (!LevelRenderEvents.BLOCK_OUTLINE_POST.invoker().onEndOutline(corelibs$context, corelibs$context)) ci.cancel();
    }

    @ModifyVariable(method = "renderHitOutline", at = @At("HEAD"), argsOnly = true)
    private VertexConsumer corelibs$resetOutlineBuffer(VertexConsumer original) {
        return corelibs$context.buffers().getBuffer(RenderType.lines());
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/debug/DebugRenderer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;DDD)V", ordinal = 0))
    private void corelibs$debugRenderPre(CallbackInfo ci) {
        LevelRenderEvents.DEBUG_RENDER_PRE.invoker().onStartDebugRender(corelibs$context);
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;F)V"))
    private void corelibs$onRenderParticles(CallbackInfo ci) {
        corelibs$didRenderParticles = true;
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V"))
    private void corelibs$translucentPost(CallbackInfo ci) {
        if (corelibs$didRenderParticles) {
            corelibs$didRenderParticles = false;
            LevelRenderEvents.TRANSLUCENT_POST.invoker().onEndTranslucent(corelibs$context);
        }
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderDebug(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/Camera;)V"))
    private void corelibs$renderLast(CallbackInfo ci) {
        LevelRenderEvents.LAST.invoker().onLast(corelibs$context);
    }

    @Inject(method = "renderLevel", at = @At("RETURN"))
    private void corelibs$renderEnd(CallbackInfo ci) {
        LevelRenderEvents.END.invoker().onEnd(corelibs$context);
    }
}