package lib.plenilune.client.render.gui.image;

import java.util.ArrayList;
import java.util.Collection;

import com.mojang.blaze3d.vertex.PoseStack;

import lib.plenilune.client.render.gui.GuiRender;

public class ImageBuffer {
	protected ArrayList<ImageDataSource> data = new ArrayList<>();

	public void render(PoseStack poseStack, int x1, int x2, int y1, int y2) {
		poseStack.pushPose();
		int finalReplaceBackgroundIndex = -1;// 在REPLACE模式下最终要绘制的ImageDataSource的索引
		int currentSourcePriority = -1;// 当前ImageDataSource的优先级
		for (int source_idx = 0; source_idx < data.size(); ++source_idx) {
			ImageDataSource source = data.get(source_idx);
			if (currentSourcePriority <= source.drawPriority) {
				switch (source.drawMode) {
				case REPLACE: {
					// 替换模式下只记录最终的ImageDataSource索引并绘制它
					finalReplaceBackgroundIndex = source_idx;
				}
					break;
				case OVERLAY: {
					// 覆盖模式下就直接在原ImageDataSource上继续绘制
					if (source.imageAreas != null)
						for (int i = 0; i < source.imageAreas.length; ++i) {
							ImageArea area = source.imageAreas[i];
							GuiRender.blitImage(poseStack, area.resourceLoc, x1, x2, y1, y2, area.depth, area.u1, area.u2, area.v1, area.v2);
						}
				}
					break;
				}
			}
		}
		// 如果有ImageDataSource是替换模式，则只绘制优先级最高的的ImageDataSource
		if (!(finalReplaceBackgroundIndex < 0)) {
			ImageDataSource source = data.get(finalReplaceBackgroundIndex);
			for (int i = 0; i < source.imageAreas.length; ++i) {
				ImageArea area = source.imageAreas[i];
				GuiRender.blitImage(poseStack, area.resourceLoc, x1, x2, y1, y2, area.depth, area.u1, area.u2, area.v1, area.v2);
			}
		}
		poseStack.popPose();
	}

	public boolean add(ImageDataSource source) {
		return data.add(source);
	}

	public boolean remove(ImageDataSource source) {
		return data.remove(source);
	}

	public boolean removeAll(Collection<ImageDataSource> source) {
		return data.removeAll(source);
	}

	public ImageDataSource remove(int source_idx) {
		return data.remove(source_idx);
	}

	public int size() {
		return data.size();
	}

	public ImageBuffer clear() {
		data.clear();
		return this;
	}
}
