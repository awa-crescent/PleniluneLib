package lib.plenilune.client.render.model.transform;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lib.plenilune.client.render.item.ItemRender;
import lib.plenilune.client.render.item.ItemRendererRenderFunc;
import lib.plenilune.item.ItemUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ItemModelsTransform {
	private static ArrayList<ItemModelTransformFunc> itemModelsTransform = new ArrayList<>();

	public static final ItemRendererRenderFunc itemModelsTransformFunc = new ItemRendererRenderFunc() {
		@Override
		public void render(ItemRenderer itemRender, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
			for (ItemModelTransformFunc transform_func : itemModelsTransform) {
				if (transform_func.condition(itemStack, ItemUtils.getID(itemStack.getItem()), displayContext, leftHand))
					transform_func.transform(itemStack, displayContext, leftHand, poseStack, model);
			}
		};
	};

	static {
		ItemRender.add_after_modify_model_before_translate_Func(itemModelsTransformFunc);
	}

	public static void addItemModelTransform(ItemModelTransformFunc func) {
		itemModelsTransform.add(func);
	}
}
