package io.github.apace100.origins.client;

import io.github.apace100.origins.power.IPower;
import io.github.apace100.origins.power.Power;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class PowerClient implements IPower {
	
	private Power power;
	
	public PowerClient(Power wrap) {
		this.power = wrap;
	}
	
	protected Power getPower() {
		return this.power;
	}

	@Override
	public CompoundNBT getPowerData(PlayerEntity player) {
		return power.getPowerData(player);
	}

	@Override
	public boolean isActive(LivingEntity player) {
		return power.isActive(player);
	}

	@Override
	public boolean isActive(PlayerEntity player) {
		return power.isActive(player);
	}
	
	
}
