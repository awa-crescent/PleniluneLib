package lib.plenilune.client.render;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import lib.lunar.jvm.Manipulator;
import lib.lunar.jvm.Reflect;
import lib.lunar.nativemc.Version;

public class TesselatorInstance {
	public static Tesselator instance;
	public static Method Tesselator_begin = null;

	private static final int version_case;

	static {
		instance = Tesselator.getInstance();
		if (Version.this_version.between("1_20_R0", "1_21_R0")) {
			version_case = 0;
		} else if (Version.this_version.newerOrEqual("1_21_R0")) {
			version_case = 1;
			Tesselator_begin = Reflect.getMethod(Tesselator.class, "method_60827", new Class<?>[] { VertexFormat.Mode.class, VertexFormat.class });
		} else
			version_case = -1;
	}

	public static BufferBuilder getBufferBuilder(VertexFormat.Mode mode, VertexFormat format) {
		BufferBuilder builder = null;
		switch (version_case) {
		case 0: {
			builder = (BufferBuilder) Manipulator.access(instance, "field_1574");// Tesselator.builder
			// BufferBuilder.begin(VertexFormat$Mode arg0, VertexFormat arg1)
			Manipulator.invoke(builder, "method_1328", new Class<?>[] { VertexFormat.Mode.class, VertexFormat.class }, mode, format);
		}
			break;
		case 1: {
			try {
				builder = (BufferBuilder) Tesselator_begin.invoke(instance, mode, format);
			} catch (IllegalAccessException | InvocationTargetException ex) {
				ex.printStackTrace();
			}
		}
			break;
		}
		return builder;
	}

	// <------------ Vertex------------->
	public static BufferBuilder addVertex(BufferBuilder bufferBuilder, Matrix4f pose, float x, float y, float z) {
		// 1.20.1 VertexConsumer.vertex(org.joml.Matrix4f arg0, float arg1, float arg2, float arg3)
		// 1.21.1 VertexConsumer.addvertex(org.joml.Matrix4f arg0, float arg1, float arg2, float arg3)
		Manipulator.invoke(bufferBuilder, "method_22918", new Class<?>[] { Matrix4f.class, float.class, float.class, float.class }, pose, x, y, z);
		return bufferBuilder;
	}

	public static BufferBuilder setUv(BufferBuilder bufferBuilder, float u, float v) {
		// 1.20.1 VertexConsumer.uv(float arg0, float arg1)
		// 1.21.1 BufferBuilder.setUv(float arg0, float arg1) 两个方法名称均为method_22913
		Manipulator.invoke(bufferBuilder, "method_22913", new Class<?>[] { float.class, float.class }, u, v);
		return bufferBuilder;
	}

	/**
	 * 添加一个含位置和UV坐标的顶点
	 * 
	 * @param bufferBuilder
	 * @param pose
	 * @param x
	 * @param y
	 * @param z
	 * @param u
	 * @param v
	 */
	public static void posUvVertex(BufferBuilder bufferBuilder, Matrix4f pose, float x, float y, float z, float u, float v) {
		switch (version_case) {
		case 0: {
			setUv(addVertex(bufferBuilder, pose, x, y, z), u, v);
			// 1.20.1 VertexConsumer.endVertex()
			Manipulator.invoke(bufferBuilder, "method_1344", null);
		}
			break;
		case 1: {
			setUv(addVertex(bufferBuilder, pose, x, y, z), u, v);
		}
			break;
		}
	}

	public static BufferBuilder setColor(BufferBuilder bufferBuilder, float r, float g, float b, float a) {
		// 1.20.1 VertexConsumer.color(float arg0, float arg1, float arg2, float arg3)
		// 1.21.1 VertexConsumer.setColor(float arg0, float arg1, float arg2, float arg3)
		switch (version_case) {
		case 0: {
			Manipulator.invoke(bufferBuilder, "color", new Class<?>[] { float.class, float.class, float.class, float.class }, r, g, b, a);
		}
			break;
		case 1: {
			Manipulator.invoke(bufferBuilder, "setColor", new Class<?>[] { float.class, float.class, float.class, float.class }, r, g, b, a);
		}
			break;
		}
		return bufferBuilder;
	}

	public static void posUvColorVertex(BufferBuilder bufferBuilder, Matrix4f pose, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
		switch (version_case) {
		case 0: {
			setColor(setUv(addVertex(bufferBuilder, pose, x, y, z), u, v), r, g, b, a);
			// 1.20.1 VertexConsumer.endVertex()
			Manipulator.invoke(bufferBuilder, "endVertex", null);
		}
			break;
		case 1: {
			setColor(setUv(addVertex(bufferBuilder, pose, x, y, z), u, v), r, g, b, a);
		}
			break;
		}
	}

	public static void posUvColorVertex(BufferBuilder bufferBuilder, Matrix4f pose, float x, float y, float z, float u, float v) {
		posUvColorVertex(bufferBuilder, pose, x, y, z, u, v, 1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static void drawBufferBuilder(BufferBuilder bufferBuilder) {
		switch (version_case) {
		case 0: {
			BufferUploader_drawWithShader(Manipulator.invoke(bufferBuilder, "method_1326", null));// end()
		}
			break;
		case 1: {
			BufferUploader_drawWithShader(Manipulator.invoke(bufferBuilder, "method_60800", null));// buildOrThrow()
		}
			break;
		}
	}

	public static void BufferUploader_drawWithShader(Object bufferBuilder) {
		switch (version_case) {
		case 0: {
			// drawWithShader(BufferBuilder$RenderedBuffer arg0)
			Manipulator.invoke(BufferUploader.class, "method_43433", new Class<?>[] { Reflect.getClassForName("net.minecraft.class_287$class_7433") }, bufferBuilder);
		}
			break;
		case 1: {
			// drawWithShader(MeshData arg0)
			Manipulator.invoke(BufferUploader.class, "method_43433", new Class<?>[] { Reflect.getClassForName("net.minecraft.class_9801") }, bufferBuilder);
		}
			break;
		}
	}
}
