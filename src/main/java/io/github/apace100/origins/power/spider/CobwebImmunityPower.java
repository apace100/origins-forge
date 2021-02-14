package io.github.apace100.origins.power.spider;

import io.github.apace100.origins.power.Power;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class CobwebImmunityPower extends Power {

	public CobwebImmunityPower(String name) {
		super(name);
		this.setTicking();
	}

	@Override
	public void onTick(PlayerEntity player) {
		if(player.world.isMaterialInBB(player.getBoundingBox(), Material.WEB)) {
			Vec3d motion = player.getMotion();
			player.setMotion(motion.x * 4, motion.y * 20, motion.z * 4);
			player.velocityChanged = true;
		}
	}

	@Override
	public void onTickPost(PlayerEntity player) {
		//if(player.world.isMaterialInBB(player.getBoundingBox(), Material.WEB)) {
		//	player.setMotionMultiplier(null, new Vec3d(1, 1, 1));
		//}
	}
}
