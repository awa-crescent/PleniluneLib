package lib.plenilune.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lib.plenilune.client.render.entity.EntityRender;
import lib.plenilune.client.render.entity.EntityRenderDispatcherRenderFunc;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;

@Mixin(targets = { "net.minecraft.client.renderer.entity.EntityRenderDispatcher" })
public abstract class EntityRenderDispatcherMixin implements ResourceManagerReloadListener {

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = Shift.BEFORE), cancellable = true)
	private void before_render_Funcs(Entity entity, double x_offset, double y_offset, double z_offset, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightCoords, CallbackInfo ci) {
		for (EntityRenderDispatcherRenderFunc func : EntityRender.before_EntityRenderDispatcher_render_popPose_Funcs)
			func.render((EntityRenderDispatcher) (Object) this, entity, x_offset, y_offset, z_offset, entityYaw, partialTick, poseStack, multiBufferSource, packedLightCoords, ci);
	}
}
