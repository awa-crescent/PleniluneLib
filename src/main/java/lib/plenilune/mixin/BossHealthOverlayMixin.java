package lib.plenilune.mixin;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lib.plenilune.client.render.gui.BossHealthOverlayRender;
import lib.plenilune.client.render.gui.BossHealthOverlayRenderFunc;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;

@Mixin(targets = { "net.minecraft.client.gui.components.BossHealthOverlay" })
public class BossHealthOverlayMixin {
	@Final
	Map<UUID, LerpingBossEvent> events;

	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	public void render(GuiGraphics guiGraphics, CallbackInfo ci) {
		for (BossHealthOverlayRenderFunc func : BossHealthOverlayRender.before_render_Funcs)
			func.render((BossHealthOverlay) (Object) this, events, guiGraphics, ci);
	}

	@Inject(method = "render", at = @At(value = "RETURN", shift = At.Shift.BEFORE), cancellable = true)
	public void before_render_drawBar_Funcs(GuiGraphics guiGraphics, CallbackInfo ci) {
		for (BossHealthOverlayRenderFunc func : BossHealthOverlayRender.before_render_return_Funcs)
			func.render((BossHealthOverlay) (Object) this, events, guiGraphics, ci);
	}

}
