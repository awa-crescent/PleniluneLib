package lib.plenilune.client.render;

import java.lang.reflect.Field;
import java.util.Deque;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;

import lib.lunar.jvm.Manipulator;
import lib.lunar.jvm.Reflect;

public class PoseStackManipulator {
	private static final Field f_poseStack;

	static {
		f_poseStack = Reflect.getField(PoseStack.class, "poseStack");
	}

	/**
	 * 获取PoseStack内部的Pose栈
	 * 
	 * @param poseStack
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Deque<PoseStack.Pose> innerPoseStack(PoseStack poseStack) {
		return (Deque<Pose>) Manipulator.access(poseStack, f_poseStack);
	}

	/**
	 * 在PoseStack顶层推入指定Pose
	 * 
	 * @param poseStack
	 * @param pose
	 */
	public static void pushPose(PoseStack poseStack, PoseStack.Pose pose) {
		innerPoseStack(poseStack).addLast(pose);
	}
}
