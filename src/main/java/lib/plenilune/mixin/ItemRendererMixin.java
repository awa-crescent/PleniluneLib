package lib.plenilune.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lib.plenilune.client.render.item.ItemRender;
import lib.plenilune.client.render.item.ItemRendererRenderFunc;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Mixin(targets = { "net.minecraft.client.renderer.entity.ItemRenderer" })
public abstract class ItemRendererMixin implements ResourceManagerReloadListener {
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = Shift.BEFORE), cancellable = true)
	private void before_render_Funcs(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
		for (ItemRendererRenderFunc func : ItemRender.before_render_Funcs)
			func.render((ItemRenderer) (Object) this, itemStack, displayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, model, ci);// (GuiGraphics) (Object) this用于trick编译器使其强制转换编译通过，this实际上正是GuiGraphics对象本体
	}

	@Inject(method = "render", at = @At(value = "RETURN"), cancellable = true)
	private void before_render_return_Funcs(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
		for (ItemRendererRenderFunc func : ItemRender.before_render_return_Funcs)
			func.render((ItemRenderer) (Object) this, itemStack, displayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, model, ci);
	}

	// 在render()中pose.translate()前前调用
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", shift = At.Shift.BY, by = -3), cancellable = true)
	private void before_render_model_translate_Funcs(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
		for (ItemRendererRenderFunc func : ItemRender.before_render_model_translate_Funcs)
			func.render((ItemRenderer) (Object) this, itemStack, displayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, model, ci);
	}

	// 在before_render_model_translate_Funcs()、render()中pose.translate()前修改model变量
	@ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", shift = At.Shift.BY, by = -2), index = 8)
	private BakedModel modify_model_before_translate(BakedModel model) {
		if (ItemRender.currentFinalModel != null) {
			BakedModel final_model = ItemRender.currentFinalModel;
			ItemRender.currentFinalModel = null;
			return final_model;
		} else
			return model;
	}

	// 在modify_model_before_translate()后、pose.translate()前调用
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", shift = At.Shift.BY, by = -1), cancellable = true)
	private void after_modify_model_before_translate_Funcs(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
		for (ItemRendererRenderFunc func : ItemRender.after_modify_model_before_translate_Funcs)
			func.render((ItemRenderer) (Object) this, itemStack, displayContext, leftHand, poseStack, bufferSource, combinedLight, combinedOverlay, model, ci);
	}
}
