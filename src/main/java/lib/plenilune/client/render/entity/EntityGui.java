package lib.plenilune.client.render.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import lib.plenilune.client.render.PoseStackManipulator;
import lib.plenilune.client.render.gui.Gui;
import lib.plenilune.client.render.level.LevelRender;
import lib.plenilune.client.render.level.LevelRendererRenderFunc;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;

public class EntityGui {
	public static HashMap<Class<?>, ArrayList<Gui>> guisMap = new HashMap<>();
	public static ArrayList<GuiEntityInfo> collectedGuiEntities = new ArrayList<>();

	private static class GuiEntityInfo {
		public EntityRenderDispatcher entityRenderDispatcher;
		public PoseStack.Pose pose;
		public Entity entity;
		public ArrayList<Gui> guis;
		public float entityYaw;
		public float partialTick;

		GuiEntityInfo(EntityRenderDispatcher entityRenderDispatcher, PoseStack.Pose pose, Entity entity, ArrayList<Gui> guis, float entityYaw, float partialTick) {
			this.entityRenderDispatcher = entityRenderDispatcher;
			this.pose = pose;
			this.entity = entity;
			this.guis = guis;
			this.entityYaw = entityYaw;
			this.partialTick = partialTick;
		}

		@Override
		public String toString() {
			return "{entity=" + entity + ", pose=" + pose + ", entityYaw=" + entityYaw + ", partialTick=" + partialTick + "}";
		}
	}

	// 在系统渲染实体时收集实体信息，待全部渲染任务完成后再渲染GUI，防止GUI被其他渲染的东西覆盖
	public static EntityRenderDispatcherRenderFunc collectGuiEntitiesFunc = new EntityRenderDispatcherRenderFunc() {
		@Override
		public void render(EntityRenderDispatcher entityRenderDispatcher, Entity entity, double x_offset, double y_offset, double z_offset, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightCoords, CallbackInfo ci) {
			for (Entry<Class<?>, ArrayList<Gui>> entry : guisMap.entrySet()) {
				if (entry.getKey().isInstance(entity)) {
					ArrayList<Gui> guis = entry.getValue();
					GuiEntityInfo info = new GuiEntityInfo(entityRenderDispatcher, poseStack.last(), entity, guis, entityYaw, partialTick);
					collectedGuiEntities.add(info);
				}
			}
		};
	};

	public static LevelRendererRenderFunc renderingEntityGuiFunc = new LevelRendererRenderFunc() {
		@Override
		public void renderLevel(LevelRenderer levelRenderer, PoseStack poseStack, float partialTick, long l, boolean renderOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
			for (GuiEntityInfo entry : collectedGuiEntities) {
				for (Gui gui : entry.guis) {
					PoseStackManipulator.pushPose(poseStack, entry.pose);
					poseStack.translate(0, gui.entityGuiDrawHeight(entry.entity), 0);// 移动原点
					poseStack.mulPose(entry.entityRenderDispatcher.cameraOrientation());// 实体GUI坐标系中原点在实体头上正中心，向左向上为正
					// 将实体GUI坐标系转换为寻常GUI坐标系，寻常GUI坐标系原点在左上角，向右向下为正
					gui.onEntityGuiDraw(entry.entityRenderDispatcher, entry.entity, entry.entityYaw, entry.partialTick);
					gui.render(poseStack, gui.transformEntityGuiCoordX(gui.entityGuiDrawOffsetX(entry.entity)), gui.transformEntityGuiCoordX(gui.width + gui.entityGuiDrawOffsetX(entry.entity)), gui.transformEntityGuiCoordY(gui.entityGuiDrawOffsetY(entry.entity)), gui.transformEntityGuiCoordY(gui.height) + gui.entityGuiDrawOffsetY(entry.entity));
					gui.dispose();
					poseStack.popPose();
				}
			}
			collectedGuiEntities.clear();// 渲染完成后清除实体列表
		};
	};

	public static void attachEntityGui(Class<?> entity, Gui gui) {
		if (guisMap.containsKey(entity))
			guisMap.get(entity).add(gui);
		else {
			ArrayList<Gui> guis = new ArrayList<>();
			guis.add(gui);
			guisMap.put(entity, guis);
		}
	}

	static {
		EntityRender.add_before_EntityRenderDispatcher_render_popPose_Func(collectGuiEntitiesFunc);
		LevelRender.add_before_renderLevel_1st_checkPoseStack_Func(renderingEntityGuiFunc);
	}
}
