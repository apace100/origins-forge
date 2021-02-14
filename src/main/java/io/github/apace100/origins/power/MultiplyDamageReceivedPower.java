package io.github.apace100.origins.power;

import java.util.function.Predicate;

import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MultiplyDamageReceivedPower extends Power {

	private Predicate<DamageSource> applicableDamagePredicate;
	private float multiplier;
	
	public MultiplyDamageReceivedPower(String name, Predicate<DamageSource> damageType, float multiplier) {
		super(name);
		this.setEventHandler();
		this.multiplier = multiplier;
		this.applicableDamagePredicate = damageType;
	}

	@SubscribeEvent
	public void onTakeDamage(LivingDamageEvent event) {
		if(isActive(event.getEntityLiving()) && applicableDamagePredicate.test(event.getSource())) {
			event.setAmount(event.getAmount() * multiplier);
		}
	}
}
