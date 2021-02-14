package io.github.apace100.origins.power.common;

import io.github.apace100.origins.enchantment.WaterProtectionEnchantment;
import io.github.apace100.origins.power.Power;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;

public class HurtfulWaterPower extends Power {

	private static final int TIME_BEFORE_HURT = 20;
	
	private static final int HURT_DELAY_WITH_ENCHANTMENT = 3 * 20;
	
	public HurtfulWaterPower(String name) {
		super(name);
		this.setTicking();
	}

	@Override
	public void onTick(PlayerEntity player) {
		if(!player.world.isRemote) {
			CompoundNBT powerData = ((CompoundNBT)getPowerData(player));
			int time = getTime(powerData);
			if(player.isInWaterRainOrBubbleColumn()) {
				if(time <= 0) {
					player.attackEntityFrom(DamageSource.DROWN, 1.0F);
					setTime(powerData, TIME_BEFORE_HURT);
				} else {
					time -= 1;
					setTime(powerData, time);
				}
			} else {
				if(time < getMaxTime(player)) {
					setTime(powerData, getMaxTime(player));
				}
			}
		}
	}
	
	private int getMaxTime(PlayerEntity player) {
		int level = 0;
		for(ItemStack is : player.getArmorInventoryList()) {
			level += EnchantmentHelper.getEnchantmentLevel(WaterProtectionEnchantment.INSTANCE, is);
		}
		return TIME_BEFORE_HURT + level * HURT_DELAY_WITH_ENCHANTMENT;
	}

	private void setTime(CompoundNBT nbt, int value) {
		nbt.putInt("Time", value);
	}

	private int getTime(CompoundNBT nbt) {
		return nbt.getInt("Time");
	}
	
	@Override
	public CompoundNBT createPowerData() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("Time", TIME_BEFORE_HURT);
		return nbt;
	}
}
