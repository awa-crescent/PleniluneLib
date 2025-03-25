package lib.plenilune.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lib.plenilune.client.render.entity.EntityRender;
import lib.plenilune.client.render.entity.EntityRendererRenderFunc;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;

@Mixin(targets = { "net.minecraft.client.renderer.entity.EntityRenderer" })
public abstract class EntityRendererMixin<T extends Entity> {
	@SuppressWarnings("rawtypes")
	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	private void before_render_shouldShowName(Entity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
		for (EntityRendererRenderFunc func : EntityRender.before_render_shouldShowName)
			func.render((EntityRenderer) (Object) this, entity, entityYaw, partialTick, poseStack, bufferSource, packedLight, ci);// (GuiGraphics) (Object) this用于trick编译器使其强制转换编译通过，this实际上正是GuiGraphics对象本体
	}
}