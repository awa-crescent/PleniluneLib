package lib.plenilune.mixin;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lib.plenilune.client.render.level.LevelRender;
import lib.plenilune.client.render.level.LevelRendererRenderFunc;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

@Mixin(targets = { "net.minecraft.client.renderer.LevelRenderer" })
public abstract class LevelRendererMixin implements ResourceManagerReloadListener, AutoCloseable {
	@Inject(method = "renderLevel", at = @At(value = "RETURN", shift = Shift.BEFORE), cancellable = true)
	private void after_renderLevel_Funcs(PoseStack poseStack, float partialTick, long l, boolean renderOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
		for (LevelRendererRenderFunc func : LevelRender.after_renderLevel_Funcs)
			func.renderLevel((LevelRenderer) (Object) this, poseStack, partialTick, l, renderOutline, camera, gameRenderer, lightTexture, matrix4f, ci);
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0, shift = Shift.BEFORE), cancellable = true)
	private void before_renderLevel_1st_checkPoseStack_Funcs(PoseStack poseStack, float partialTick, long l, boolean renderOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
		for (LevelRendererRenderFunc func : LevelRender.before_renderLevel_1st_checkPoseStack_Funcs)
			func.renderLevel((LevelRenderer) (Object) this, poseStack, partialTick, l, renderOutline, camera, gameRenderer, lightTexture, matrix4f, ci);
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V", ordinal = 2, shift = Shift.BEFORE), cancellable = true)
	private void before_renderLevel_3rd_applyModelViewMatrix_Funcs(PoseStack poseStack, float partialTick, long l, boolean renderOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
		for (LevelRendererRenderFunc func : LevelRender.before_renderLevel_3rd_applyModelViewMatrix_Funcs)
			func.renderLevel((LevelRenderer) (Object) this, poseStack, partialTick, l, renderOutline, camera, gameRenderer, lightTexture, matrix4f, ci);
	}
}
