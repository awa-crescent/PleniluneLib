package lib.plenilune.client.render.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lib.lunar.jvm.Manipulator;
import lib.plenilune.PleniluneLib;
import lib.plenilune.client.render.item.ItemRender;
import lib.plenilune.client.render.item.ItemRendererRenderFunc;
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
			PleniluneLib.LOGGER.info(item_id + " light=" + combinedLight + " overlay=" + combinedOverlay);
			HashMap<String, ItemModelRenderingInfo> map = itemModels.get(displayContext.getId());
			if (map.containsKey(item_id)) {
				ItemModelRenderingInfo info = map.get(item_id);
				ItemRender.currentFinalModel = itemRender.getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(info.resourceLoc));
				// 如果指定渲染物品类型，则需要更改ItemStack的item成员变量，因为该参数和model同样关乎渲染
				if (info.itemType != null) {
					ItemRender.currentOriginalRenderItem = (Item) Manipulator.access(itemStack, "field_8038");// 储存当前的ItemStack.item
					Manipulator.setObject(itemStack, "field_8038", info.itemType);// 修改目标item成员
				}
			}
		};
	};

	public static ItemRendererRenderFunc itemTypeRecoveryFunc = new ItemRendererRenderFunc() {
		@Override
		public void render(ItemRenderer itemRender, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
			// 如果修改了渲染的item，则需要在渲染结束后改回去
			if (ItemRender.currentOriginalRenderItem != null) {
				Manipulator.setObject(itemStack, "field_8038", ItemRender.currentOriginalRenderItem);// ItemStack.item
				ItemRender.currentOriginalRenderItem = null;
			}
		};
	};

	// 如果JVM没有加载此类，那么modelReplaceFunc注入方法将不会被执行
	static {
		int context_num = ItemDisplayContext.values().length;
		for (int i = 0; i < context_num; ++i)
			itemModels.add(new HashMap<>());
		ItemRender.add_before_render_model_getTransform_Func(modelReplaceFunc);
		ItemRender.add_before_render_return_Func(itemTypeRecoveryFunc);
	}

	public static void setItemModel(String item_id, ItemModelRenderingInfo info, ItemDisplayContext context) {
		itemModels.get(context.getId()).put(item_id, info);
	}

	public static void setItemModel(String item_id, ItemModelRenderingInfo info, String context) {
		setItemModel(item_id, info, ItemRender.parseItemDisplayContext(context));
	}

	public static void setInventoryModel(String item_id, ItemModelRenderingInfo info) {
		setItemModel(item_id, info, ItemDisplayContext.GUI);
		setItemModel(item_id, info, ItemDisplayContext.GROUND);
		setItemModel(item_id, info, ItemDisplayContext.FIXED);
	}

	public static void setInventoryModel(String item_id, ResourceLocation model_resloc, Item item) {
		setInventoryModel(item_id, new ItemModelRenderingInfo(model_resloc, item));
	}

	public static void setInventoryModel(String item_id, ResourceLocation model_resloc) {
		setInventoryModel(item_id, new ItemModelRenderingInfo(model_resloc));
	}

	public static void setInventoryModel(String item_id, String model_resloc) {
		setInventoryModel(item_id, ResourceLocation.parse(model_resloc));
	}

	public static void setInventoryModel(String item_id, String model_resloc, Item item) {
		setInventoryModel(item_id, ResourceLocation.parse(model_resloc), item);
	}

	// 物品渲染模型设置
	public static void setInHandModel(String item_id, ItemModelRenderingInfo info) {
		setItemModel(item_id, info, ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
		setItemModel(item_id, info, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
		setItemModel(item_id, info, ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
		setItemModel(item_id, info, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
	}

	public static void setInHandModel(String item_id, ResourceLocation model_resloc, Item item) {
		setInHandModel(item_id, new ItemModelRenderingInfo(model_resloc, item));
	}

	public static void setInHandModel(String item_id, ResourceLocation model_resloc) {
		setInHandModel(item_id, new ItemModelRenderingInfo(model_resloc));
	}

	public static void setInHandModel(String item_id, String model_resloc) {
		setInHandModel(item_id, ResourceLocation.parse(model_resloc));
	}

	public static void setInHandModel(String item_id, String model_resloc, Item item) {
		setInHandModel(item_id, ResourceLocation.parse(model_resloc), item);
	}

	public static ItemModelRenderingInfo getItemModel(String item_id, ItemDisplayContext context) {
		HashMap<String, ItemModelRenderingInfo> map = itemModels.get(context.getId());
		return map.containsKey(item_id) ? map.get(item_id) : null;
	}
}
