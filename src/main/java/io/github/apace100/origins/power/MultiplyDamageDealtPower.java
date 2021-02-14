package io.github.apace100.origins.power;

import java.util.function.Predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MultiplyDamageDealtPower extends Power {

	private Predicate<DamageSource> applicableDamagePredicate;
	private float multiplier;
	
	public MultiplyDamageDealtPower(String name, Predicate<DamageSource> damageType, float multiplier) {
		super(name);
		this.setEventHandler();
		this.multiplier = multiplier;
		this.applicableDamagePredicate = damageType;
	}

	@SubscribeEvent
	public void onTakeDamage(LivingDamageEvent event) {
		if(event.getSource().getTrueSource() instanceof PlayerEntity) {
			if(isActive((PlayerEntity)event.getSource().getTrueSource()) && applicableDamagePredicate.test(event.getSource())) {
				event.setAmount(event.getAmount() * multiplier);
			}
		}
		
	}
}
