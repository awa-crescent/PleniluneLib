package lib.plenilune.client.render.gui.impl;

import java.util.ArrayList;

import lib.plenilune.client.render.gui.Gui;
import lib.plenilune.client.render.gui.image.ImageDataSource;

public class Bar extends Gui {
	public static class Style {
		protected ImageDataSource bg;
		protected ImageDataSource head;
		protected ImageDataSource body;
		protected ImageDataSource tail;

		protected float bg_x1_offset = 0;// 条相对于条的背景的位移
		protected float bg_y1_offset = 0;
		protected float bg_x2_offset = 0;
		protected float bg_y2_offset = 0;
		protected float head_width = 0;
		protected float tail_width = 0;

		public final Style setBarBgOffset(float x1_offset, float y1_offset, float x2_offset, float y2_offset) {
			this.bg_x1_offset = x1_offset;
			this.bg_y1_offset = y1_offset;
			this.bg_x2_offset = x2_offset;
			this.bg_y2_offset = y2_offset;
			return this;
		}

		// 条的头部
		public final Style setBarHead(ImageDataSource bar_head) {
			this.head = bar_head;
			return this;
		}

		public final Style setBarHead(ImageDataSource bar_head, float head_width) {
			this.head = bar_head;
			this.head_width = head_width;
			return this;
		}

		// 条的中部，可变长度，位移依据BarHead而定
		public final Style setBarBody(ImageDataSource bar_body) {
			this.body = bar_body;
			return this;
		}

		// 条的尾部，位移依据BarHead、BarBody而定
		public final Style setBarTail(ImageDataSource bar_tail, float tail_width) {
			this.tail = bar_tail;
			this.tail_width = tail_width;
			return this;
		}

		// 条的背景
		public final Style setBg(ImageDataSource bg) {
			this.bg = bg;
			return this;
		}
	}

	ArrayList<Style> styles = new ArrayList<>();

	public Bar() {
	}

	public Bar(Style... styles) {
		for (int i = 0; i < styles.length; ++i)
			this.styles.add(styles[i]);
	}

	public final Bar addStyle(Style style) {
		styles.add(style);
		return this;
	}

	public final Bar setProgress(int style_idx, float progress_percentage) {
		guiComponents.clear();
		Style style = styles.get(style_idx);
		if (progress_percentage <= 0) {
			super.guiComponents.add(style.bg);
		} else {
			super.guiComponents.add(style.bg);
			super.guiComponents.add(style.head.setRenderingOffset(transformEntityGuiCoord_dX(style.bg_x1_offset), transformEntityGuiCoord_dY(style.bg_y1_offset), transformEntityGuiCoord_dX(style.bg_x1_offset + style.head_width - width), transformEntityGuiCoord_dY(style.bg_y2_offset)));
			super.guiComponents.add(style.body.setRenderingOffset(transformEntityGuiCoord_dX(width - style.head.x2_offset), style.head.y1_offset, style.head.x2_offset + transformEntityGuiCoord_dX((width + style.bg_x2_offset - style.bg_x1_offset - style.head_width - style.tail_width) * progress_percentage), style.head.y2_offset));
			super.guiComponents.add(style.tail.setRenderingOffset(transformEntityGuiCoord_dX(width - style.body.x2_offset), style.body.y1_offset, style.body.x2_offset + transformEntityGuiCoord_dX(style.tail_width), style.body.y2_offset));
		}
		return this;
	}

	@Override
	public Bar setSize(float width, float height) {
		return (Bar) super.setSize(width, height);
	}
}
