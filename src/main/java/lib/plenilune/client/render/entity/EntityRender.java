package lib.plenilune.client.render.entity;

import java.util.ArrayList;

public class EntityRender {
	// render()中model.getTransform()前
	public static ArrayList<EntityRendererRenderFunc> before_render_shouldShowName = new ArrayList<>();

	public static void add_before_render_shouldShowName_Func(EntityRendererRenderFunc func) {
		before_render_shouldShowName.add(func);
	}
}
