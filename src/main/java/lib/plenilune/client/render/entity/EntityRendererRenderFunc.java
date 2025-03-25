package lib.plenilune.client.render.entity;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;

public interface EntityRendererRenderFunc {
	/**
	 * 在渲染物品前执行的代码，需要被覆写。 其中x、y为待渲染物品的左上角顶点坐标
	 * 
	 * @param pose      全局矩阵变换
	 * @param entity
	 * @param level
	 * @param stack     要渲染的物品stack
	 * @param x         渲染的x坐标，以GUI左上角为原点，向右为正
	 * @param y         渲染的y坐标，以GUI左上角为原点，向下为正
	 * @param seed
	 * @param guiOffset 渲染的深度
	 */
	@SuppressWarnings("rawtypes")
	public default void render(EntityRenderer entityRender, Entity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
	};
}
