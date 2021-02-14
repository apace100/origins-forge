package io.github.apace100.origins.power.spider;

import io.github.apace100.origins.power.Power;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InflictPoisonPower extends Power {

	public InflictPoisonPower(String name) {
		super(name);
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onLivingDamage(LivingDamageEvent event) {
		if(event.getAmount() > 0f && event.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getSource().getTrueSource();
			if(isActive(player)) {
				event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.POISON, 5 * 20, 0));
			}
		}
	}
}
