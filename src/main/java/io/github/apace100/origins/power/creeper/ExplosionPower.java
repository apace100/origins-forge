package io.github.apace100.origins.power.creeper;

import io.github.apace100.origins.network.NetworkHelper;
import io.github.apace100.origins.power.Powers;
import io.github.apace100.origins.power.UsablePower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Explosion.Mode;

public class ExplosionPower extends UsablePower {

	private static final float fillInBars = 1F;
	public ExplosionPower(String name) {
		super(name, 10);
	}

	@Override
	public void perform(PlayerEntity player) {
		float value = Powers.GUNPOWDER_RESOURCE.getFillInBars(player);
		if(value <= 7 - fillInBars) {
			Powers.GUNPOWDER_RESOURCE.changeValueInBars(player, fillInBars);
			NetworkHelper.syncOrigin(player);
			player.world.createExplosion(player, player.getPosX(), player.getPosY(), player.getPosZ(), 3F, Mode.BREAK);
		}
	}

}
