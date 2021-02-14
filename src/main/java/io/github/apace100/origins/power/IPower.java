package io.github.apace100.origins.power;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public interface IPower {
	public CompoundNBT getPowerData(PlayerEntity player);
	
	public boolean isActive(LivingEntity player);
	public boolean isActive(PlayerEntity player);
}
