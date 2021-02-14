package io.github.apace100.origins.power.phantom;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.Powers;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NoAttacksPower extends Power {

	public NoAttacksPower(String name) {
		super(name);
		this.setEventHandler();
	}

	
	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent event) {
		if(isActive(event.getPlayer()) && Powers.INVISIBILITY.isInvisible(event.getPlayer())) {
			event.setCanceled(true);
		}
	}
	
	/*
	@SubscribeEvent
	public void onPlayerDealDamage(LivingDamageEvent event) {
		Entity source = event.getSource().getTrueSource();
		if(source != null && source instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)source;
			if(isActive(player)) {
				event.setAmount(0F);
			}
		}
	}
	*/
}
