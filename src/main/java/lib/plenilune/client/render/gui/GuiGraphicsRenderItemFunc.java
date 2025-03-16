package lib.plenilune.client.render.gui;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface GuiGraphicsRenderItemFunc {
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
	public default void renderItem(GuiGraphics guiGraphics, LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
	};
}
