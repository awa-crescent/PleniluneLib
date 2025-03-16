package lib.plenilune.client.render.gui;

import java.util.ArrayList;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class GuiRender {

	public static ArrayList<GuiGraphicsRenderItemFunc> before_renderItem_Funcs = new ArrayList<>();

	public static void add_before_renderItem_Func(GuiGraphicsRenderItemFunc func) {
		before_renderItem_Funcs.add(func);
	}

	/**
	 * 绘制平面图像，不操作PoseStack，需要手动操作栈
	 * 
	 * @param poseStack
	 * @param atlasLocation
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param blitOffset
	 * @param minU
	 * @param maxU
	 * @param minV
	 * @param maxV
	 */
	public static void blitImage(PoseStack poseStack, net.minecraft.resources.ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
		RenderSystem.setShaderTexture(0, (atlasLocation));
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f matrix4f = poseStack.last().pose();
		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.addVertex(matrix4f, (float) x1, (float) y1, (float) blitOffset).setUv(minU, minV);
		bufferBuilder.addVertex(matrix4f, (float) x1, (float) y2, (float) blitOffset).setUv(minU, maxV);
		bufferBuilder.addVertex(matrix4f, (float) x2, (float) y2, (float) blitOffset).setUv(maxU, maxV);
		bufferBuilder.addVertex(matrix4f, (float) x2, (float) y1, (float) blitOffset).setUv(maxU, minV);
		BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
	}

	public static void blitImage(PoseStack poseStack, net.minecraft.resources.ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset) {
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, blitOffset, 0, 1, 0, 1);
	}

	public static void blitImage(PoseStack poseStack, net.minecraft.resources.ResourceLocation atlasLocation, int x1, int x2, int y1, int y2) {
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, 1, 0, 1, 0, 1);
	}

	public static void blitImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
		blitImage(poseStack, ResourceLocation.parse(atlasLocation), x1, x2, y1, y2, blitOffset, minU, maxU, minV, maxV);
	}

	public static void blitImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2, int blitOffset) {
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, blitOffset, 0, 1, 0, 1);
	}

	public static void blitImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2) {
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, 1, 0, 1, 0, 1);
	}

	/**
	 * 绘制平面图像，自动推入弹出栈，不改变原有的PoseStack
	 * 
	 * @param poseStack
	 * @param atlasLocation
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param blitOffset
	 * @param minU
	 * @param maxU
	 * @param minV
	 * @param maxV
	 */
	public static void drawImage(PoseStack poseStack, net.minecraft.resources.ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
		poseStack.pushPose();
		blitImage(poseStack, atlasLocation, x1, x2, y1, y2, blitOffset, minU, maxU, minV, maxV);
		poseStack.popPose();
	}

	public static void drawImage(PoseStack poseStack, net.minecraft.resources.ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset) {
		drawImage(poseStack, atlasLocation, x1, x2, y1, y2, blitOffset, 0, 1, 0, 1);
	}

	public static void drawImage(PoseStack poseStack, net.minecraft.resources.ResourceLocation atlasLocation, int x1, int x2, int y1, int y2) {
		drawImage(poseStack, atlasLocation, x1, x2, y1, y2, 1, 0, 1, 0, 1);
	}

	public static void drawImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
		drawImage(poseStack, ResourceLocation.parse(atlasLocation), x1, x2, y1, y2, blitOffset, minU, maxU, minV, maxV);
	}

	public static void drawImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2, int blitOffset) {
		drawImage(poseStack, atlasLocation, x1, x2, y1, y2, blitOffset, 0, 1, 0, 1);
	}

	public static void drawImage(PoseStack poseStack, String atlasLocation, int x1, int x2, int y1, int y2) {
		drawImage(poseStack, atlasLocation, x1, x2, y1, y2, 1, 0, 1, 0, 1);
	}

	/**
	 * 在GUI的物品格子中渲染图像
	 * 
	 * @param poseStack
	 * @param atlasLocation
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @param blitOffset
	 * @param minU
	 * @param maxU
	 * @param minV
	 * @param maxV
	 */
	public static void drawImageInSlot(PoseStack poseStack, net.minecraft.resources.ResourceLocation atlasLocation, int x, int y, int blitOffset, float minU, float maxU, float minV, float maxV) {
		poseStack.pushPose();
		blitImage(poseStack, atlasLocation, x, x + 16, y, y + 16, blitOffset, minU, maxU, minV, maxV);
		poseStack.popPose();
	}

	public static void drawImageInSlot(PoseStack poseStack, net.minecraft.resources.ResourceLocation atlasLocation, int x, int y, float minU, float maxU, float minV, float maxV) {
		drawImageInSlot(poseStack, atlasLocation, x, y, 1, minU, maxU, minV, maxV);
	}

	public static void drawImageInSlot(PoseStack poseStack, net.minecraft.resources.ResourceLocation atlasLocation, int x, int y, int blitOffset) {
		drawImageInSlot(poseStack, atlasLocation, x, y, blitOffset, 0, 1, 0, 1);
	}

	public static void drawImageInSlot(PoseStack poseStack, net.minecraft.resources.ResourceLocation atlasLocation, int x, int y) {
		drawImageInSlot(poseStack, atlasLocation, x, y, 1, 0, 1, 0, 1);
	}

	public static void drawImageInSlot(PoseStack poseStack, String atlasLocation, int x, int y, int blitOffset, float minU, float maxU, float minV, float maxV) {
		drawImageInSlot(poseStack, ResourceLocation.parse(atlasLocation), x, y, blitOffset, minU, maxU, minV, maxV);
	}

	public static void drawImageInSlot(PoseStack poseStack, String atlasLocation, int x, int y, int blitOffset) {
		drawImageInSlot(poseStack, atlasLocation, x, y, blitOffset, 0, 1, 0, 1);
	}

	public static void drawImageInSlot(PoseStack poseStack, String atlasLocation, int x, int y) {
		drawImageInSlot(poseStack, atlasLocation, x, y, 1, 0, 1, 0, 1);
	}

	public static void drawImageInSlot(PoseStack poseStack, String atlasLocation, int x, int y, float minU, float maxU, float minV, float maxV) {
		drawImageInSlot(poseStack, atlasLocation, x, y, 1, minU, maxU, minV, maxV);
	}
}
