package lib.plenilune.client.render.entity;

import java.util.ArrayList;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;

public class EntityRender {
	// render()执行前
	public static ArrayList<EntityRendererRenderFunc> before_render_Funcs = new ArrayList<>();

	public static void add_before_render_Func(EntityRendererRenderFunc func) {
		before_render_Funcs.add(func);
	}

	public static EntityRenderDispatcher entityRenderDispatcher = null;

	public static ArrayList<EntityRenderDispatcherRenderFunc> before_EntityRenderDispatcher_render_popPose_Funcs = new ArrayList<>();

	public static void add_before_EntityRenderDispatcher_render_popPose_Func(EntityRenderDispatcherRenderFunc func) {
		before_EntityRenderDispatcher_render_popPose_Funcs.add(func);
	}
}
