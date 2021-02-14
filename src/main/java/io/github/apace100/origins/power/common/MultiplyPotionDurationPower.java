package io.github.apace100.origins.power.common;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.utils.Reflection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class MultiplyPotionDurationPower extends Power {

	private double multiplier;
	
	public MultiplyPotionDurationPower(String name, double multiplier) {
		super(name);
		this.multiplier = multiplier;
		this.setEventHandler();
	}
	
	@SubscribeEvent
	public void onApplyPotion(PotionAddedEvent event) {
		if(!event.getEntity().world.isRemote) {
			if(event.getEntityLiving() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity)event.getEntityLiving();
				if(this.isActive(player) && event.getPotionEffect().getPotion().isBeneficial()) {
					int newDuration = (int)(event.getPotionEffect().getDuration() * this.multiplier);
					ObfuscationReflectionHelper.setPrivateValue(EffectInstance.class, event.getPotionEffect(), newDuration, Reflection.DURATION);
				}
			}
		}
	}
}
