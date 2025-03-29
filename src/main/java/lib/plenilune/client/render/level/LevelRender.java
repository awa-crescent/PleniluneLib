package lib.plenilune.client.render.level;

import java.util.ArrayList;

public class LevelRender {
	public static ArrayList<LevelRendererRenderFunc> after_renderLevel_Funcs = new ArrayList<>();

	public static void add_after_renderLevel_Func(LevelRendererRenderFunc func) {
		after_renderLevel_Funcs.add(func);
	}

	public static ArrayList<LevelRendererRenderFunc> before_renderLevel_1st_checkPoseStack_Funcs = new ArrayList<>();

	public static void add_before_renderLevel_1st_checkPoseStack_Func(LevelRendererRenderFunc func) {
		before_renderLevel_1st_checkPoseStack_Funcs.add(func);
	}

	public static ArrayList<LevelRendererRenderFunc> before_renderLevel_3rd_applyModelViewMatrix_Funcs = new ArrayList<>();

	public static void add_before_renderLevel_3rd_applyModelViewMatrix_Func(LevelRendererRenderFunc func) {
		before_renderLevel_3rd_applyModelViewMatrix_Funcs.add(func);
	}
}
