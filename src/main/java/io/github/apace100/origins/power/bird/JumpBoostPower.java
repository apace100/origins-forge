package io.github.apace100.origins.power.bird;

import io.github.apace100.origins.power.Power;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class JumpBoostPower extends Power {

	private double jumpMultiplier;
	
	public JumpBoostPower(String registryName, double jumpMultiplier) {
		super(registryName);
		this.jumpMultiplier = jumpMultiplier;
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onLivingJump(LivingJumpEvent event) {
		if(event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			if(isActive(player)) {
				Vec3d motion = player.getMotion();
				player.setMotion(motion.x, motion.y * jumpMultiplier, motion.z);
			}
		}
	}
}
