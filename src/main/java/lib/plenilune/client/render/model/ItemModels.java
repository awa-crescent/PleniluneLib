package lib.plenilune.client.render.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lib.lunar.jvm.Manipulator;
import lib.plenilune.client.render.item.ItemRender;
import lib.plenilune.client.render.item.ItemRendererRenderFunc;
import lib.plenilune.core.ResourceLocationBuilder;
import lib.plenilune.item.ItemUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ItemModels {
	// 储存了所有ItemDisplayContext各自对应的模型，以ItemDisplayContext的id为索引
	private static ArrayList<HashMap<String, ItemModelRenderingInfo>> itemModels = new ArrayList<>();

	// 渲染普通物品和方块前
	public static ItemRendererRenderFunc modelReplaceFunc = new ItemRendererRenderFunc() {
		@Override
		public void render(ItemRenderer itemRender, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
			String item_id = ItemUtils.getID(itemStack.getItem());
			HashMap<String, ItemModelRenderingInfo> map = itemModels.get(displayContext.getId());
			if (map.containsKey(item_id)) {
				ItemModelRenderingInfo info = map.get(item_id);
				// 手动指定模型路径为null将导致取消该物品的渲染
				if (info.resourceLoc == null) {
					ci.cancel();
					return;
				}
				ItemRender.currentFinalModel = itemRender.getItemModelShaper().getModelManager().getModel(new ModelResourceLocation(info.resourceLoc, "inventory"));
				// 如果指定渲染物品类型，则需要更改ItemStack的item成员变量，因为该参数和model同样关乎渲染
				if (info.itemType != null) {
					poseStack.popPose();// 先撤销poseStack的model.getTransforms().getTransform().apply()的变换
					poseStack.pushPose();// 推入原本的变换
					ItemRender.currentOriginalRenderItem = (Item) Manipulator.access(itemStack, "item");// 储存当前的ItemStack.item
					Manipulator.setObject(itemStack, "item", info.itemType);// 修改目标item成员
				}
			}
		};
	};

	public static ItemRendererRenderFunc modelTransformFunc = new ItemRendererRenderFunc() {
		@Override
		public void render(ItemRenderer itemRender, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
			if (ItemRender.currentOriginalRenderItem != null)
				model.getTransforms().getTransform(displayContext).apply(leftHand, poseStack);

		};
	};

	public static ItemRendererRenderFunc itemTypeRecoveryFunc = new ItemRendererRenderFunc() {
		@Override
		public void render(ItemRenderer itemRender, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
			// 如果修改了渲染的item，则需要在渲染结束后改回去
			if (ItemRender.currentOriginalRenderItem != null) {
				Manipulator.setObject(itemStack, "item", ItemRender.currentOriginalRenderItem);// ItemStack.item
				ItemRender.currentOriginalRenderItem = null;
			}
		};
	};

	// 如果JVM没有加载此类，那么modelReplaceFunc注入方法将不会被执行
	static {
		int context_num = ItemDisplayContext.values().length;
		for (int i = 0; i < context_num; ++i)
			itemModels.add(new HashMap<>());
		ItemRender.add_before_render_model_translate_Func(modelReplaceFunc);
		ItemRender.add_after_modify_model_before_translate_Func(modelTransformFunc);
		ItemRender.add_before_render_return_Func(itemTypeRecoveryFunc);
	}

	public static void setItemModel(String item_id, ItemDisplayContext context, ItemModelRenderingInfo info) {
		itemModels.get(context.getId()).put(item_id, info);
	}

	public static void setItemModel(String item_id, String context, String model_resloc, String item) {
		setItemModel(item_id, ItemRender.parseItemDisplayContext(context), new ItemModelRenderingInfo(model_resloc, item));
	}

	public static void setItemModel(String item_id, String context, String model_resloc) {
		setItemModel(item_id, ItemRender.parseItemDisplayContext(context), new ItemModelRenderingInfo(model_resloc, model_resloc));
	}

	public static void setInventoryModel(String item_id, ItemModelRenderingInfo info) {
		setItemModel(item_id, ItemDisplayContext.GUI, info);
		setItemModel(item_id, ItemDisplayContext.GROUND, info);
		setItemModel(item_id, ItemDisplayContext.FIXED, info);
	}

	public static void setInventoryModel(String item_id, ResourceLocation model_resloc, Item item) {
		setInventoryModel(item_id, new ItemModelRenderingInfo(model_resloc, item));
	}

	public static void setInventoryModel(String item_id, ResourceLocation model_resloc) {
		setInventoryModel(item_id, new ItemModelRenderingInfo(model_resloc));
	}

	public static void setInventoryModel(String item_id, String model_resloc, Item item) {
		setInventoryModel(item_id, ResourceLocationBuilder.getResourceLocationFromNamespacedID(model_resloc), item);
	}

	public static void setInventoryModel(String item_id, String model_resloc, String item) {
		setInventoryModel(item_id, new ItemModelRenderingInfo(model_resloc, item));
	}

	public static void setInventoryModel(String item_id, String model_resloc) {
		setInventoryModel(item_id, model_resloc, model_resloc);
	}

	// 物品渲染模型设置
	public static void setInHandModel(String item_id, ItemModelRenderingInfo info) {
		setItemModel(item_id, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, info);
		setItemModel(item_id, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, info);
		setItemModel(item_id, ItemDisplayContext.FIRST_PERSON_LEFT_HAND, info);
		setItemModel(item_id, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, info);
	}

	public static void setInHandModel(String item_id, ResourceLocation model_resloc, Item item) {
		setInHandModel(item_id, new ItemModelRenderingInfo(model_resloc, item));
	}

	public static void setInHandModel(String item_id, ResourceLocation model_resloc) {
		setInHandModel(item_id, new ItemModelRenderingInfo(model_resloc));
	}

	public static void setInHandModel(String item_id, String model_resloc, Item item) {
		setInHandModel(item_id, ResourceLocationBuilder.getResourceLocationFromNamespacedID(model_resloc), item);
	}

	public static void setInHandModel(String item_id, String model_resloc, String item) {
		setInHandModel(item_id, new ItemModelRenderingInfo(model_resloc, item));
	}

	public static void setInHandModel(String item_id, String model_resloc) {
		setInHandModel(item_id, model_resloc, model_resloc);
	}

	public static ItemModelRenderingInfo getItemModel(String item_id, ItemDisplayContext context) {
		HashMap<String, ItemModelRenderingInfo> map = itemModels.get(context.getId());
		return map.containsKey(item_id) ? map.get(item_id) : null;
	}
}
