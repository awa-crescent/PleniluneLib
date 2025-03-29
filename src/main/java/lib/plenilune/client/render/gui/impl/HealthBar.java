package lib.plenilune.client.render.gui.impl;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class HealthBar extends Bar {
	public double showRange = 64.0;// 显示的距离

	public HealthBar() {
	}

	public HealthBar(Style... styles) {
		super(styles);
	}

	@Override
	public final void onEntityGuiDraw(EntityRenderDispatcher entityRenderDispatcher, Entity entity, float entityYaw, float partialTick) {
		if (entityRenderDispatcher.distanceToSqr(entity) <= (showRange * showRange) && entity instanceof LivingEntity living) {
			onShow(living, living.getHealth() / living.getMaxHealth());
		}
	}

	// 子类可覆写的方法
	public void onShow(LivingEntity entity, float health_percentage) {
		setProgress(health_percentage);
	}

	@Override
	public HealthBar setSize(float width, float height) {
		return (HealthBar) super.setSize(width, height);
	}

	// 渲染完一帧生命条后就清除生命条数据
	@Override
	public void dispose() {
		super.guiComponents.clear();
	}

	public HealthBar setShowRange(double range) {
		this.showRange = range;
		return this;
	}
}
