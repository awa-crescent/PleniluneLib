package lib.plenilune.client.render.gui.image;

// 单个图像的图层，imageAreas的所有图层均按照其各自的depth进行带深度的绘制
public class ImageDataSource {
	// 背景优先度绘制模式
	public static enum DrawMode {
		REPLACE, OVERLAY
	}

	public static DrawMode DEFAULT_DRAWMODE = DrawMode.OVERLAY;

	public ImageArea[] imageAreas;// 该图像的图层，图层具有深度
	public int drawPriority = 0;// 该图像的整体优先级
	public DrawMode drawMode;// 当有该图像的优先度高于或等于目标时，新的图像是覆盖还是替换原图像

	public ImageDataSource() {
		this.imageAreas = null;
		this.drawMode = DEFAULT_DRAWMODE;
	}

	// ImageArea
	public ImageDataSource(ImageArea[] imageAreas, int drawPriority, DrawMode drawMode) {
		this.imageAreas = imageAreas;
		this.drawPriority = drawPriority;
		this.drawMode = drawMode;
	}

	public ImageDataSource(ImageArea[] imageAreas, int drawPriority) {
		this(imageAreas, drawPriority, DEFAULT_DRAWMODE);
	}

	public ImageDataSource(ImageArea[] imageAreas) {
		this(imageAreas, 0);
	}

	public ImageDataSource(ImageArea imageAreas, int drawPriority, DrawMode drawMode) {
		this(new ImageArea[] { imageAreas }, drawPriority, drawMode);
	}

	public ImageDataSource(ImageArea imageAreas, int drawPriority) {
		this(imageAreas, drawPriority, DEFAULT_DRAWMODE);
	}

	public ImageDataSource(ImageArea imageArea) {
		this(imageArea, 0, DEFAULT_DRAWMODE);
	}

	public ImageDataSource(net.minecraft.resources.ResourceLocation[] background_resourceloc) {
		this(ImageArea.from(background_resourceloc));
	}

	public ImageDataSource(String[] background_resourceloc) {
		this(ImageArea.from(background_resourceloc));
	}

	// 单个图像可以指定UV坐标
	// ResourceLocation
	public ImageDataSource(net.minecraft.resources.ResourceLocation background_resourceloc, float u1, float v1, float u2, float v2, int drawPriority, DrawMode drawMode) {
		this(new ImageArea[] { new ImageArea(background_resourceloc, u1, v1, u2, v2) }, drawPriority, drawMode);
	}

	public ImageDataSource(net.minecraft.resources.ResourceLocation background_resourceloc, float u1, float v1, float u2, float v2) {
		this(new ImageArea[] { new ImageArea(background_resourceloc, u1, v1, u2, v2) });
	}

	public ImageDataSource(net.minecraft.resources.ResourceLocation background_resourceloc, int drawPriority, DrawMode drawMode) {
		this(new ImageArea[] { new ImageArea(background_resourceloc) }, drawPriority, drawMode);
	}

	public ImageDataSource(net.minecraft.resources.ResourceLocation background_resourceloc) {
		this(new ImageArea[] { new ImageArea(background_resourceloc) });
	}

	// String
	public ImageDataSource(String background_resourceloc, float u1, float v1, float u2, float v2, int drawPriority, DrawMode drawMode) {
		this(net.minecraft.resources.ResourceLocation.parse(background_resourceloc), u1, v1, u2, v2, drawPriority, drawMode);
	}

	public ImageDataSource(String background_resourceloc, float u1, float v1, float u2, float v2) {
		this(net.minecraft.resources.ResourceLocation.parse(background_resourceloc), u1, v1, u2, v2);
	}

	public ImageDataSource(String background_resourceloc, int drawPriority, DrawMode drawMode) {
		this(net.minecraft.resources.ResourceLocation.parse(background_resourceloc), drawPriority, drawMode);
	}

	public ImageDataSource(String background_resourceloc) {
		this(net.minecraft.resources.ResourceLocation.parse(background_resourceloc));
	}

	public ImageDataSource setDrawPriority(int drawPriority) {
		this.drawPriority = drawPriority;
		return this;
	}

	@Override
	public String toString() {
		String descrip = "{imageAreas=[";
		for (int i = 0; i < imageAreas.length; ++i) {
			descrip += "{resourceLoc=" + imageAreas[i].resourceLoc + ", depth=" + imageAreas[i].depth + '}';
			if (i != imageAreas.length - 1)
				descrip += ", ";
		}
		descrip += "]}";
		return descrip;
	}
}