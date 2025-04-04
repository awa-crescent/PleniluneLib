package lib.plenilune.client.render.model;

import lib.plenilune.core.RegistryManager;
import lib.plenilune.core.ResourceLocationBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemModelRenderingInfo {
	public ResourceLocation resourceLoc;
	public Item itemType;
	public int combinedLight;// 光照图的UV坐标1 VertexConsumer.setUv2(combinedLight & 0x0000FFFF, combinedLight >> 16 & 0x0000FFFF);
	public int combinedOverlay;// UV坐标2 VertexConsumer.setUv1(combinedOverlay & 0x0000FFFF, combinedOverlay >> 16 & 0x0000FFFF);

	public static final int NORMAL_LIGHT = 0x00F000F0;// 普通物品在inventory上下文渲染时的光照均为该值
	public static final int BOW_LIGHT = 0x00400000;

	public static final int NO_WHITE_U = 0;
	public static final int RED_OVERLAY_V = 3;
	public static final int WHITE_OVERLAY_V = 10;
	public static final int NO_OVERLAY = 0x000A0000;// GUI渲染中所有物品均为该值

	public ItemModelRenderingInfo(ResourceLocation resourceLoc, Item itemType, int combinedLight, int combinedOverlay) {
		this.resourceLoc = resourceLoc;
		this.itemType = itemType;
		this.combinedLight = combinedLight;
		this.combinedOverlay = combinedOverlay;
	}

	public ItemModelRenderingInfo(ResourceLocation resourceLoc, Item itemType) {
		this(resourceLoc, itemType, NORMAL_LIGHT, NO_OVERLAY);
	}

	public ItemModelRenderingInfo(ResourceLocation resourceLoc) {
		this(resourceLoc, RegistryManager.item.get(resourceLoc));
	}

	// 如果显示给itemType指定为null，则取消目标模型的渲染
	public ItemModelRenderingInfo(String resourceLoc, String itemType, int combinedLight, int combinedOverlay) {
		this.resourceLoc = ResourceLocationBuilder.getResourceLocationFromNamespacedID(resourceLoc);
		this.itemType = itemType == null ? null : RegistryManager.item.get(ResourceLocationBuilder.getResourceLocationFromNamespacedID(itemType));
		this.combinedLight = combinedLight;
		this.combinedOverlay = combinedOverlay;
	}

	public ItemModelRenderingInfo(String resourceLoc, String itemType) {
		this(resourceLoc, itemType, NORMAL_LIGHT, NO_OVERLAY);
	}

	public ItemModelRenderingInfo(String resourceLoc) {
		this(resourceLoc, resourceLoc);
	}

	@Override
	public String toString() {
		return "{resourceLoc=" + resourceLoc + ", itemType=" + itemType + ", combinedLight=" + Integer.toHexString(combinedLight) + ", combinedOverlay=" + Integer.toHexString(combinedOverlay) + '}';
	}
}