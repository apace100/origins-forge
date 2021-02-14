package io.github.apace100.origins.capabilities;

import javax.annotation.Nullable;

import io.github.apace100.origins.origins.Origin;
import io.github.apace100.origins.power.Power;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public interface IOriginHolder {

	boolean hasOrigin();

	Origin getOrigin();

	INBT serializeToNBT();

	void deserializeFromNBT(INBT nbt, @Nullable PlayerEntity player);

	CompoundNBT getPowerData(Power power);

	void onAdded(PlayerEntity player);
	void onRemoved(PlayerEntity player);
	void onTick(PlayerEntity player);
	void onTickPost(PlayerEntity player);
	void onDeath(PlayerEntity player);
	
	void setOrigin(Origin origin, PlayerEntity player);

}
