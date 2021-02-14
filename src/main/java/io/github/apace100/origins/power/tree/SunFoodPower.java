package io.github.apace100.origins.power.tree;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.utils.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundEvents;

public class SunFoodPower extends Power {

	protected static final int COOLDOWN = 40;
	
	public SunFoodPower(String registryName) {
		super(registryName);
		this.setTicking();
	}

	@Override
	public void onTick(PlayerEntity player) {
		if(!player.world.isRemote && Utils.getSunlight(player.world, player.getPosition()) > 10 && player.getFoodStats().needFood()) {
			CompoundNBT powerData = ((CompoundNBT)getPowerData(player));
			int cd = getCooldown(powerData);
			if(cd <= 0) {
				FoodStats food = player.getFoodStats();
				food.setFoodLevel(food.getFoodLevel() + 1);
				player.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1f, 1f);
				setCooldown(powerData, COOLDOWN);
			} else {
				setCooldown(powerData, cd - 1);
			}
		}
	}

	private void setCooldown(CompoundNBT nbt, int value) {
		nbt.putInt("Cooldown", value);
	}

	private int getCooldown(CompoundNBT nbt) {
		return nbt.getInt("Cooldown");
	}
	
	@Override
	public CompoundNBT createPowerData() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("Cooldown", COOLDOWN);
		return nbt;
	}
}

