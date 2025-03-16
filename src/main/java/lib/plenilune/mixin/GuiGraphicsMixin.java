package lib.plenilune.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lib.plenilune.client.render.gui.GuiGraphicsRenderItemFunc;
import lib.plenilune.client.render.gui.GuiRender;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(targets = { "net.minecraft.client.gui.GuiGraphics" })
public abstract class GuiGraphicsMixin {
	@Inject(method = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.BEFORE), cancellable = true)
	private void beforeRenderItem(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
		for (GuiGraphicsRenderItemFunc func : GuiRender.before_renderItem_Funcs)
			func.renderItem((GuiGraphics) (Object) this, entity, level, stack, x, y, seed, guiOffset, ci);// (GuiGraphics) (Object) this用于trick编译器使其强制转换编译通过，this实际上正是GuiGraphics对象本体
	}
}