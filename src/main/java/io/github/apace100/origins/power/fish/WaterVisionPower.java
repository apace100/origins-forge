package io.github.apace100.origins.power.fish;

import io.github.apace100.origins.power.Power;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;

public class WaterVisionPower extends Power {
	
	public WaterVisionPower(String registryName) {
		super(registryName);
		this.setTicking();
	}

	@Override
	public void onTick(PlayerEntity player) {
		if(!player.world.isRemote) {
			if(player.isPotionActive(Effects.NIGHT_VISION)) {
				EffectInstance nvInstance = player.getActivePotionEffect(Effects.NIGHT_VISION);
				if(nvInstance.getDuration() < 32000) {
					return;
				}
			}
			if(player.areEyesInFluid(FluidTags.WATER)) {
				player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
			} else {
				player.removePotionEffect(Effects.NIGHT_VISION);
			}
		}
		
	}

	@Override
	public void onPowerRemoved(PlayerEntity player) {
		if(!player.world.isRemote) {
			if(player.isPotionActive(Effects.NIGHT_VISION)) {
				EffectInstance nvInstance = player.getActivePotionEffect(Effects.NIGHT_VISION);
				if(nvInstance.getDuration() > 32000) {
					player.removePotionEffect(Effects.NIGHT_VISION);
				}
			}
		}
	}
	
	
}

