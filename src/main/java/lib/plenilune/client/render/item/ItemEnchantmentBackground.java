package lib.plenilune.client.render.item;

import java.util.ArrayList;
import java.util.HashMap;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lib.plenilune.client.render.gui.GuiGraphicsRenderItemFunc;
import lib.plenilune.client.render.gui.GuiRender;
import lib.plenilune.client.render.gui.image.ImageArea;
import lib.plenilune.client.render.gui.image.ImageBuffer;
import lib.plenilune.client.render.gui.image.ImageDataSource;
import lib.plenilune.client.render.gui.image.ImageDataSource.DrawMode;
import lib.plenilune.item.ItemUtils;
import lib.plenilune.item.ItemUtils.EnchantmentLevel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class ItemEnchantmentBackground {
	public static DrawMode DEFAULT_DRAWMODE = DrawMode.REPLACE;

	// 单个附魔背景，包含图层和该附魔对应的背景优先级
	public static class Background extends ImageDataSource {
		public int enchantmentLevel = -1;// 显示该背景的附魔等级，小于1则所有附魔等级的装备均使用该背景

		public Background(ImageArea[] imageAreas, int drawPriority, DrawMode drawMode) {
			super(imageAreas, drawPriority, drawMode);
		}

		public Background(ImageArea[] imageAreas, int drawPriority) {
			super(imageAreas, drawPriority);
		}

		public Background(ImageArea[] imageAreas) {
			super(imageAreas);
		}

		public Background(ImageArea imageArea) {
			super(imageArea);
		}

		public Background(ImageArea imageAreas, int drawPriority, DrawMode drawMode) {
			super(imageAreas, drawPriority, drawMode);
		}

		public Background(ImageArea imageAreas, int drawPriority) {
			super(imageAreas, drawPriority);
		}

		public Background(net.minecraft.resources.ResourceLocation[] background_resourceloc) {
			super(background_resourceloc);
		}

		public Background(String[] background_resourceloc) {
			super(background_resourceloc);
		}

		// 单个图像可以指定UV坐标
		// ResourceLocation
		public Background(net.minecraft.resources.ResourceLocation background_resourceloc, float u1, float v1, float u2, float v2, int drawPriority, DrawMode drawMode) {
			super(background_resourceloc, u1, v1, u2, v2, drawPriority, drawMode);
		}

		public Background(net.minecraft.resources.ResourceLocation background_resourceloc, float u1, float v1, float u2, float v2) {
			super(background_resourceloc, u1, v1, u2, v2);
		}

		public Background(net.minecraft.resources.ResourceLocation background_resourceloc, int drawPriority, DrawMode drawMode) {
			super(background_resourceloc, drawPriority, drawMode);
		}

		public Background(net.minecraft.resources.ResourceLocation background_resourceloc) {
			super(background_resourceloc);
		}

		// String
		public Background(String background_resourceloc, float u1, float v1, float u2, float v2, int drawPriority, DrawMode drawMode) {
			super(background_resourceloc, u1, v1, u2, v2, drawPriority, drawMode);
		}

		public Background(String background_resourceloc, float u1, float v1, float u2, float v2) {
			super(background_resourceloc, u1, v1, u2, v2);
		}

		public Background(String background_resourceloc, int drawPriority, DrawMode drawMode) {
			super(background_resourceloc, drawPriority, drawMode);
		}

		public Background(String background_resourceloc) {
			super(background_resourceloc);
		}

		public Background setEnchantmentLevel(int lvl) {
			this.enchantmentLevel = lvl;
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

	private static HashMap<String, ArrayList<Background>> all_backgrounds = new HashMap<>();// Key为附魔id，Value为背景属性

	// 用于注入net.minecraft.client.gui.GuiGraphics.renderItem()的物品背景绘制代码
	public static GuiGraphicsRenderItemFunc renderingFunc = new GuiGraphicsRenderItemFunc() {
		private static ImageBuffer buffer = new ImageBuffer();

		@Override
		public void renderItem(GuiGraphics guiGraphics, LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
			ArrayList<EnchantmentLevel> enchantments_properties = ItemUtils.getAllEnchantmentsProperties(stack);
			for (EnchantmentLevel property : enchantments_properties) {
				if (all_backgrounds.containsKey(property.enchantment_id)) {
					ArrayList<Background> bgs = all_backgrounds.get(property.enchantment_id);
					for (Background bg : bgs)
						if (bg.enchantmentLevel < 1 || bg.enchantmentLevel == property.level)
							buffer.add(bg);
				}
			}
			buffer.render(guiGraphics.pose(), x, x + 16, y, y + 16);
			buffer.clear()
			;
		};
	};

	static {
		GuiRender.add_before_renderItem_Func(renderingFunc);
	}

	public static void addBackground(String enchantment_id, Background background) {
		if (all_backgrounds.containsKey(enchantment_id))
			all_backgrounds.get(enchantment_id).add(background);
		else {
			ArrayList<Background> list = new ArrayList<>();
			list.add(background);
			all_backgrounds.put(enchantment_id, list);
		}
	}

	public static void addBackground(String enchantment_id, String background_resourceloc, int enchantmentLevel, int drawPriority, DrawMode drawMode, float u1, float v1, float u2, float v2) {
		addBackground(enchantment_id, new Background(background_resourceloc, u1, v1, u2, v2, drawPriority, drawMode).setEnchantmentLevel(enchantmentLevel));
	}

	public static void addBackground(String enchantment_id, String background_resourceloc, int enchantmentLevel, int drawPriority, float u1, float v1, float u2, float v2) {
		addBackground(enchantment_id, new Background(background_resourceloc, u1, v1, u2, v2, 0, DEFAULT_DRAWMODE).setEnchantmentLevel(enchantmentLevel));
	}

	public static void addBackground(String enchantment_id, String background_resourceloc, int enchantmentLevel, int drawPriority, DrawMode drawMode) {
		addBackground(enchantment_id, new Background(background_resourceloc, drawPriority, drawMode).setEnchantmentLevel(enchantmentLevel));
	}

	public static void addBackground(String enchantment_id, String background_resourceloc, int enchantmentLevel, int drawPriority) {
		addBackground(enchantment_id, new Background(background_resourceloc, drawPriority, DEFAULT_DRAWMODE).setEnchantmentLevel(enchantmentLevel));
	}

	public static void addBackground(String enchantment_id, String background_resourceloc, float u1, float v1, float u2, float v2) {
		addBackground(enchantment_id, new Background(background_resourceloc, u1, v1, u2, v2, 0, DEFAULT_DRAWMODE));
	}

	public static void addBackground(String enchantment_id, String background_resourceloc) {
		addBackground(enchantment_id, new Background(background_resourceloc, 0, DEFAULT_DRAWMODE));
	}

	// ImageArea
	public static void addBackground(String enchantment_id, ImageArea imageArea, int enchantmentLevel, int drawPriority, DrawMode drawMode) {
		addBackground(enchantment_id, new Background(imageArea, drawPriority, drawMode).setEnchantmentLevel(enchantmentLevel));
	}

	public static void addBackground(String enchantment_id, ImageArea imageArea, int enchantmentLevel, int drawPriority) {
		addBackground(enchantment_id, new Background(imageArea, drawPriority, DEFAULT_DRAWMODE).setEnchantmentLevel(enchantmentLevel));
	}

	public static void addBackground(String enchantment_id, ImageArea imageArea, int enchantmentLevel) {
		addBackground(enchantment_id, new Background(imageArea, 0, DEFAULT_DRAWMODE).setEnchantmentLevel(enchantmentLevel));
	}

	public static void addBackground(String enchantment_id, ImageArea imageArea) {
		addBackground(enchantment_id, new Background(imageArea, 0, DEFAULT_DRAWMODE));
	}

	// 获取指定附魔的背景列表
	public static ArrayList<Background> getBackgrounds(String enchantment_id) {
		return all_backgrounds.containsKey(enchantment_id) ? all_backgrounds.get(enchantment_id) : null;
	}
}
