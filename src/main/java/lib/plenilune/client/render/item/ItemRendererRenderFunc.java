package lib.plenilune.client.render.item;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public interface ItemRendererRenderFunc {
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
	public default void render(ItemRenderer itemRender, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
	};
}
