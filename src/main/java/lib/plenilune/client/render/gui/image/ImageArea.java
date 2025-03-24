package lib.plenilune.client.render.gui.image;

import lib.plenilune.core.ResourceLocationBuilder;

public class ImageArea {
	public net.minecraft.resources.ResourceLocation resourceLoc;
	public float u1;
	public float v1;
	public float u2;
	public float v2;
	public int depth;// 绘制深度，深度高的在上层

	public ImageArea(net.minecraft.resources.ResourceLocation resourceLoc, float u1, float v1, float u2, float v2, int depth) {
		this.resourceLoc = resourceLoc;
		this.u1 = u1;
		this.v1 = v1;
		this.u2 = u2;
		this.v2 = v2;
		this.depth = depth;
	}

	public ImageArea(net.minecraft.resources.ResourceLocation resourceLoc, float u1, float v1, float u2, float v2) {
		this(resourceLoc, u1, v1, u2, v2, 0);
	}

	public ImageArea(net.minecraft.resources.ResourceLocation resourceLoc) {
		this(resourceLoc, 0, 0, 1, 1);
	}

	public static ImageArea from(net.minecraft.resources.ResourceLocation resourceLoc) {
		return new ImageArea(resourceLoc);
	}

	public static ImageArea from(String resourceLoc) {
		return new ImageArea(ResourceLocationBuilder.getResourceLocationFromNamespacedID(resourceLoc));
	}

	public static ImageArea[] from(net.minecraft.resources.ResourceLocation[] resourceLocs) {
		ImageArea[] areas = new ImageArea[resourceLocs.length];
		for (int i = 0; i < resourceLocs.length; ++i)
			areas[i] = new ImageArea(resourceLocs[i]);
		return areas;
	}

	public static ImageArea[] from(String[] resourceLocs) {
		ImageArea[] areas = new ImageArea[resourceLocs.length];
		for (int i = 0; i < resourceLocs.length; ++i)
			areas[i] = new ImageArea(ResourceLocationBuilder.getResourceLocationFromNamespacedID(resourceLocs[i]));
		return areas;
	}
}
