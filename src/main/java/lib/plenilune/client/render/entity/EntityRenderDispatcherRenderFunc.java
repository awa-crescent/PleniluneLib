package lib.plenilune.client.render.entity;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;

public interface EntityRenderDispatcherRenderFunc {
	public default void render(EntityRenderDispatcher entityRenderDispatcher, Entity entity, double x_offset, double y_offset, double z_offset, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightCoords, CallbackInfo ci) {

	}
}
