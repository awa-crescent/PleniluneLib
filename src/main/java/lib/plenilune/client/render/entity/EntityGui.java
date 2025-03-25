package lib.plenilune.client.render.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lib.lunar.jvm.Manipulator;
import lib.plenilune.client.render.gui.Gui;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;

public class EntityGui {
	public static HashMap<Class<? extends Entity>, ArrayList<Gui>> guisMap = new HashMap<>();

	public static EntityRendererRenderFunc renderingEntityGuiFunc = new EntityRendererRenderFunc() {
		@SuppressWarnings("rawtypes")
		@Override
		public void render(EntityRenderer entityRender, Entity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
			for (Entry<Class<? extends Entity>, ArrayList<Gui>> entry : guisMap.entrySet()) {
				if (entry.getKey().isInstance(entity)) {
					ArrayList<Gui> guis = entry.getValue();
					for (Gui gui : guis) {
						poseStack.pushPose();
						poseStack.translate(0, gui.entityGuiDrawHeight(entity), 0);// 移动原点
						EntityRenderDispatcher entityRenderDispatcher = (EntityRenderDispatcher) Manipulator.access(entityRender, "entityRenderDispatcher");// EntityRenderer.entityRenderDispatcher
						poseStack.mulPose(entityRenderDispatcher.cameraOrientation());// 实体GUI坐标系中原点在实体头上正中心，向左向上为正
						// 将实体GUI坐标系转换为寻常GUI坐标系，寻常GUI坐标系原点在左上角，向右向下为正
						gui.onEntityGuiDraw(entityRenderDispatcher, entity, entityYaw, partialTick);
						gui.render(poseStack, gui.transformEntityGuiCoordX(gui.entityGuiDrawOffsetX(entity)), gui.transformEntityGuiCoordX(gui.width + gui.entityGuiDrawOffsetX(entity)), gui.transformEntityGuiCoordY(gui.entityGuiDrawOffsetY(entity)), gui.transformEntityGuiCoordY(gui.height) + gui.entityGuiDrawOffsetY(entity));
						gui.dispose();
						poseStack.popPose();
					}
				}
			}
		};
	};

	public static void attachEntityGui(Class<? extends Entity> entity, Gui gui) {
		if (guisMap.containsKey(entity))
			guisMap.get(entity).add(gui);
		else {
			ArrayList<Gui> guis = new ArrayList<>();
			guis.add(gui);
			guisMap.put(entity, guis);
		}
	}

	static {
		EntityRender.add_before_render_shouldShowName_Func(renderingEntityGuiFunc);
	}
}
