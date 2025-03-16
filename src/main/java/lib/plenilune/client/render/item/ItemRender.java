package lib.plenilune.client.render.item;

import java.util.ArrayList;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;

public class ItemRender {
	public static BakedModel currentFinalModel = null;
	public static Item currentOriginalRenderItem = null;

	// render()渲染前
	public static ArrayList<ItemRendererRenderFunc> before_render_Funcs = new ArrayList<>();

	public static void add_before_render_Func(ItemRendererRenderFunc func) {
		before_render_Funcs.add(func);
	}

	// render()中model.getTransform()前
	public static ArrayList<ItemRendererRenderFunc> before_render_model_getTransform_Funcs = new ArrayList<>();

	public static void add_before_render_model_getTransform_Func(ItemRendererRenderFunc func) {
		before_render_model_getTransform_Funcs.add(func);
	}

	// render()返回前
	public static ArrayList<ItemRendererRenderFunc> before_render_return_Funcs = new ArrayList<>();

	public static void add_before_render_return_Func(ItemRendererRenderFunc func) {
		before_render_return_Funcs.add(func);
	}

	public static ItemDisplayContext parseItemDisplayContext(String context_str) {
		ItemDisplayContext context = null;
		switch (context_str) {
		case "thirdperson_lefthand":
			context = ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
			break;
		case "thirdperson_righthand":
			context = ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
			break;
		case "firstperson_lefthand":
			context = ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
			break;
		case "firstperson_righthand":
			context = ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
			break;
		case "head":
			context = ItemDisplayContext.HEAD;
			break;
		case "gui":
			context = ItemDisplayContext.GUI;
			break;
		case "ground":
			context = ItemDisplayContext.GROUND;
			break;
		case "fixed":
			context = ItemDisplayContext.FIXED;
			break;
		default:
			context = ItemDisplayContext.NONE;
			break;
		}
		return context;
	}
}
