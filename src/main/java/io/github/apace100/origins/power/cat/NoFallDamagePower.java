package io.github.apace100.origins.power.cat;

import io.github.apace100.origins.power.Power;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NoFallDamagePower extends Power {

	public NoFallDamagePower(String name) {
		super(name);
		this.setEventHandler();
	}
	
	@SubscribeEvent
	public void onAttacked(LivingAttackEvent event) {
		if(event.getSource() == DamageSource.FALL && isActive(event.getEntityLiving())) {
			event.setCanceled(true);
		}
	}

}
