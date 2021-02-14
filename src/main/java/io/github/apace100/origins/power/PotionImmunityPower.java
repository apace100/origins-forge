package io.github.apace100.origins.power;

import com.google.common.collect.ImmutableSet;

import net.minecraft.potion.Effect;
import net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PotionImmunityPower extends Power {

	private ImmutableSet<Effect> effects;
	
	public PotionImmunityPower(String name, Effect... effects) {
		super(name);
		this.effects = ImmutableSet.copyOf(effects);
		this.setEventHandler();
	}

	@SubscribeEvent
	public void isPotionApplicable(PotionApplicableEvent event) {
		if(isActive(event.getEntityLiving())) {
			if(effects.contains(event.getPotionEffect().getPotion())) {
				event.setResult(Result.DENY);
			}
		}
	}
}
