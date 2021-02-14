package io.github.apace100.origins.power.fire;

import io.github.apace100.origins.power.Power;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NoMeleeDamagePower extends Power {

	public NoMeleeDamagePower(String name) {
		super(name);
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onPlayerDealDamage(LivingDamageEvent event) {
		Entity source = event.getSource().getTrueSource();
		if(!event.getSource().isProjectile() && source != null && source instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)source;
			if(isActive(player)) {
				event.setAmount(0F);
			}
		}
	}
}
