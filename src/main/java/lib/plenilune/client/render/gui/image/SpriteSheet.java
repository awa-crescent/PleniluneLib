package lib.plenilune.client.render.gui.image;

public class SpriteSheet {
	private final net.minecraft.resources.ResourceLocation resourceLoc;;
	private final float sprite_width;
	private final float sprite_height;

	// x、y坐标系为图像左上角为原点，向右向下为正。UV坐标系以图像左下角为原点，向右向上为正
	public final float start_offset_x;// 最左上角sprite的左上角像素相对于图像左上角的x坐标位移（归一化）
	public final float start_offset_y;// 最左上角sprite的左上角像素相对于图像左上角的y坐标位移（归一化）
	public final float end_offset_x;// 最右下角sprite的右下角像素相对于图像左上角的x坐标位移（归一化）
	public final float end_offset_y;// 最右下角sprite的右下角像素相对于图像左上角的y坐标位移（归一化）
	public final float stride_x;// 左右相邻两个sprite之间的U坐标差
	public final float stride_y;// 上下相邻两个sprite之间的V坐标差
	public final int row;// 有多少行
	public final int column;// 有多少列

	public SpriteSheet(net.minecraft.resources.ResourceLocation resourceLoc) {
		this(resourceLoc, 1, 1, 0, 0, 0, 0, 1, 1);
	}

	public SpriteSheet(net.minecraft.resources.ResourceLocation resourceLoc, int row, int column) {
		this(resourceLoc, row, column, 0, 0, 0, 0, 1, 1);
	}

	public SpriteSheet(net.minecraft.resources.ResourceLocation resourceLoc, int row, int column, float stride_x, float stride_y) {
		this(resourceLoc, row, column, stride_x, stride_y, 0, 0, 1, 1);
	}

	public SpriteSheet(net.minecraft.resources.ResourceLocation resourceLoc, int row, int column, float stride_x, float stride_y, float start_offset_x, float start_offset_y, float end_offset_x, float end_offset_y) {
		this.resourceLoc = resourceLoc;
		this.row = row;
		this.column = column;
		this.stride_x = stride_x;
		this.stride_y = stride_y;
		this.start_offset_x = start_offset_x;
		this.start_offset_y = start_offset_y;
		this.end_offset_x = end_offset_x;
		this.end_offset_y = end_offset_y;
		sprite_width = (end_offset_x - start_offset_x - stride_x * (column - 1)) / column;
		sprite_height = (end_offset_y - start_offset_y - stride_y * (row - 1)) / row;
	}

	public SpriteSheet(String resourceLoc) {
		this(resourceLoc, 1, 1, 0, 0, 0, 0, 1, 1);
	}

	public SpriteSheet(String resourceLoc, int row, int column) {
		this(resourceLoc, row, column, 0, 0, 0, 0, 1, 1);
	}

	public SpriteSheet(String resourceLoc, int row, int column, float stride_x, float stride_y) {
		this(resourceLoc, row, column, stride_x, stride_y, 0, 0, 1, 1);
	}

	public SpriteSheet(String resourceLoc, int row, int column, float stride_x, float stride_y, float start_offset_x, float start_offset_y, float end_offset_x, float end_offset_y) {
		this(net.minecraft.resources.ResourceLocation.parse(resourceLoc), row, column, stride_x, stride_y, start_offset_x, start_offset_y, end_offset_x, end_offset_y);
	}

	public ImageArea clipImage(int idx_x, int idx_y) {
		if (idx_x < column && idx_y < row)
			return new ImageArea(resourceLoc, start_offset_x + (sprite_width + stride_x) * idx_x, start_offset_y + (sprite_height + stride_y) * idx_y, start_offset_x + (sprite_width + stride_x) * idx_x + sprite_width, start_offset_y + (sprite_height + stride_y) * idx_y + sprite_height);
		else
			return null;
	}
}
