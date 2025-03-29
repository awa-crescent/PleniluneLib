package lib.plenilune.client.render.gui;

import java.util.ArrayList;

public class BossHealthOverlayRender {
	public static ArrayList<BossHealthOverlayRenderFunc> before_render_Funcs = new ArrayList<>();

	public static void add_before_render_Func(BossHealthOverlayRenderFunc func) {
		before_render_Funcs.add(func);
	}

	public static ArrayList<BossHealthOverlayRenderFunc> before_render_return_Funcs = new ArrayList<>();

	public static void add_before_render_return_Func(BossHealthOverlayRenderFunc func) {
		before_render_return_Funcs.add(func);
	}
}
