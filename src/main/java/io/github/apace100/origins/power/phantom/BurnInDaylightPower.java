package io.github.apace100.origins.power.phantom;

import io.github.apace100.origins.power.Power;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class BurnInDaylightPower extends Power {

	public BurnInDaylightPower(String name) {
		super(name);
		this.setTicking();
	}

	@Override
	public void onTick(PlayerEntity player) {
		if(areConditionsFulfilled(player) && isInDaylight(player)) {
			player.setFire(8);
		}
	}
	
	protected boolean isInDaylight(PlayerEntity player) {
		if (player.world.isDaytime() && !player.world.isRemote) {
			float f = player.getBrightness();
			BlockPos blockpos = player.getRidingEntity() instanceof BoatEntity ? (new BlockPos(player.getPosX(), (double)Math.round(player.getPosY()), player.getPosZ())).up() : new BlockPos(player.getPosX(), (double)Math.round(player.getPosY()), player.getPosZ());
			if (f > 0.5F && player.getRNG().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && player.world.canSeeSky(blockpos)) {
				return true;
			}
		}

		return false;
	}
}
