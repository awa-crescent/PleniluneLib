package lib.plenilune.client.render.gui;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;

public interface BossHealthOverlayRenderFunc {
	public default void render(BossHealthOverlay bossHealthOverlay, Map<UUID, LerpingBossEvent> events, GuiGraphics guiGraphics, CallbackInfo ci) {

	}

	public default void render_before_drawBar(BossHealthOverlay bossHealthOverlay, LerpingBossEvent lerpingBossEvent, GuiGraphics guiGraphics, CallbackInfo ci) {

	}
}
