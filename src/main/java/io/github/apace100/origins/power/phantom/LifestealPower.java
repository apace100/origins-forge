package io.github.apace100.origins.power.phantom;

import io.github.apace100.origins.power.Power;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LifestealPower extends Power {

	private float multiplier;
	
	public LifestealPower(String name, float multiplier) {
		super(name);
		this.multiplier = multiplier;
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onDealDamage(LivingDamageEvent event) {
		if(!event.getSource().isProjectile()) {
			if(event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity)event.getSource().getTrueSource();
				if(isActive(player)) {
					float heal = event.getAmount() * multiplier;
					player.heal(heal);
				}
			}
		}
		
	}
}
