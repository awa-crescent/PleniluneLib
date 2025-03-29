package lib.plenilune.client.render.gui;

import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;

import lib.plenilune.client.render.gui.image.ImageBuffer;
import lib.plenilune.client.render.gui.image.ImageDataSource;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;

public class Gui {
	protected static ImageBuffer buffer = new ImageBuffer();

	public ArrayList<ImageDataSource> guiComponents = new ArrayList<>();

	public float width;
	public float height;

	public Gui(ImageDataSource... dataSources) {
		for (int i = 0; i < dataSources.length; ++i)
			guiComponents.add(dataSources[i]);
	}

	public Gui setSize(float width, float height) {
		this.width = width;
		this.height = height;
		return this;
	}

	/**
	 * 忽略width、height进行GUI渲染
	 * 
	 * @param poseStack
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 */
	public void render(PoseStack poseStack, float x1, float x2, float y1, float y2) {
		for (ImageDataSource component : guiComponents) {
			if (component == null)
				continue;
			buffer.add(component);
		}
		buffer.render(poseStack, x1, x2, y1, y2);
		buffer.clear();
	}

	public void render(MultiBufferSource bufferSource, RenderType type, PoseStack poseStack, float x1, float x2, float y1, float y2) {
		for (ImageDataSource component : guiComponents) {
			if (component == null)
				continue;
			buffer.add(component);
		}
		buffer.render(bufferSource, type, poseStack, x1, x2, y1, y2);
		buffer.clear();
	}

	// 渲染一帧结束后的操作
	public void dispose() {

	}

	public void render(PoseStack poseStack, float x, float y) {
		render(poseStack, x, x + width, y, y + height);
	}

	// 实体GUI坐标系，向左向上为正；寻常GUI坐标系原点在左上角，向右向下为正
	// 将实体GUI坐标系与寻常GUI坐标系互相转换
	public final float transformEntityGuiCoordX(float x) {
		return width / 2 - x;
	}

	public final float transformEntityGuiCoordY(float y) {
		return height - y;
	}

	// 将寻常GUI坐标系转换为
	public final float transformEntityGuiCoord_dX(float x) {
		return -x;
	}

	public final float transformEntityGuiCoord_dY(float y) {
		return -y;
	}

	// 实体附加GUI的相关回调
	public void onEntityGuiDraw(EntityRenderDispatcher entityRenderDispatcher, Entity entity, float entityYaw, float partialTick) {

	}

	public float entityGuiDrawHeight(Entity entity) {
		return entity.getBbHeight() + height;
	}

	public float entityGuiDrawOffsetX(Entity entity) {
		return 0;
	}

	public float entityGuiDrawOffsetY(Entity entity) {
		return 0;
	}
}
