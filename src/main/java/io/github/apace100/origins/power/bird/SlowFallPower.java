package io.github.apace100.origins.power.bird;

import io.github.apace100.origins.power.Power;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class SlowFallPower extends Power {

	private double velocityCap;
	
	public SlowFallPower(String name, double maxNegativeYVelocity) {
		super(name);
		this.velocityCap = maxNegativeYVelocity;
		this.setTicking();
	}

	@Override
	public void onTick(PlayerEntity player) {
		super.onTick(player);
		Vec3d motion = player.getMotion();
		if(motion.y < -velocityCap && !player.isCrouching()) {
			player.setMotion(motion.x, -velocityCap, motion.z);
			player.fallDistance = 0f;
		}
	}

	
}
