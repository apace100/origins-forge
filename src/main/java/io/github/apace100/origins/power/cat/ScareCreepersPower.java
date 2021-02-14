package io.github.apace100.origins.power.cat;

import io.github.apace100.origins.power.Power;

public class ScareCreepersPower extends Power {

	public ScareCreepersPower(String name) {
		super(name);
		this.setEventHandler();
	}
	/*
	@SubscribeEvent
	public void onSetTarget(LivingSetAttackTargetEvent event) {
		if(event.getEntityLiving() instanceof CreeperEntity) {
			if(isActive(event.getTarget())) {
				event.setCanceled(true);
				// This does not work, rather extend AI by switching to TargetSelector with predicate
			}
		}
	}
	*/
}
