package io.github.apace100.origins.power.tree;

import io.github.apace100.origins.power.Power;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BurnForeverPower extends Power {

	public BurnForeverPower(String name) {
		super(name);
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onTakeDamage(LivingAttackEvent event) {
		if(!event.getEntity().world.isRemote) {
			if(event.getSource().isFireDamage()) {
				if(isActive(event.getEntityLiving())) {
					if(event.getEntityLiving().isBurning()) {
						event.getEntityLiving().setFire(60);
					}
				}
			}
		}
	}
}
