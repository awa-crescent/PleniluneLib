package lib.plenilune.client.render.gui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lib.plenilune.client.render.gui.impl.Bar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;

public class BossHealthOverlayGui {
	public static HashMap<String, Bar> barGuisMap = new HashMap<>();
	// 临时储存events中的自定义Bar事件，防止Minecraft在后续重复渲染同一个事件
	public static LinkedHashMap<UUID, LerpingBossEvent> isolated = new LinkedHashMap<>();

	public static final BossHealthOverlayRenderFunc customGuiRenderingFunc = new BossHealthOverlayRenderFunc() {
		@Override
		public void render(BossHealthOverlay bossHealthOverlay, Map<UUID, LerpingBossEvent> events, GuiGraphics guiGraphics, CallbackInfo ci) {
			Minecraft client = Minecraft.getInstance();
			if (!events.isEmpty()) {
				client.getProfiler().push("bossHealth");
				int i = guiGraphics.guiWidth();
				int j = 12;
				for (Entry<UUID, LerpingBossEvent> entry : events.entrySet()) {
					UUID eventId = entry.getKey();
					LerpingBossEvent lerpingBossEvent = entry.getValue();
					String eventName = lerpingBossEvent.getName().getString();
					if (barGuisMap.containsKey(eventName)) {
						int k = i / 2 - 91;
						Bar bar = barGuisMap.get(eventName);
						if (bar != null) {// 显示指定Bar为null则取消渲染该BossBar
							bar.setProgress(lerpingBossEvent.getProgress());
							bar.render(guiGraphics.pose(), k, j);
						}
						isolated.put(eventId, lerpingBossEvent);
						events.remove(eventId);
					}
				}
				client.getProfiler().pop();
			}
		}
	};

	public static final BossHealthOverlayRenderFunc recoveryIsolatedEventsFunc = new BossHealthOverlayRenderFunc() {
		@Override
		public void render(BossHealthOverlay bossHealthOverlay, Map<UUID, LerpingBossEvent> events, GuiGraphics guiGraphics, CallbackInfo ci) {
			if (!isolated.isEmpty()) {
				events.putAll(isolated);
				isolated.clear();
			}
		}
	};

	static {
		BossHealthOverlayRender.add_before_render_Func(customGuiRenderingFunc);
		BossHealthOverlayRender.add_before_render_return_Func(recoveryIsolatedEventsFunc);
	}

	public static void replaceBossBar(String bossEvent, Bar bar) {
		barGuisMap.put(bossEvent, bar);
	}
}
