package io.github.apace100.origins.power;

import io.github.apace100.origins.effect.VenomEffect;
import io.github.apace100.origins.utils.Reflection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class VenomPower extends Power{

	private float amount;
	private float multiplier;

	public VenomPower(String name, float amount, float multiplier) {
		super(name);
		this.amount = amount;
		this.multiplier = multiplier;
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onDealDamage(LivingDamageEvent event) {
		if(event.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getSource().getTrueSource();
			if(isActive(player)) {
				float venom = event.getAmount() * amount * multiplier;
				float decimal = venom - (int)venom;
				float residual = event.getAmount() * (1F - amount) + decimal;
				System.out.println("Venom: " + venom);
				event.getEntityLiving().addPotionEffect(new EffectInstance(VenomEffect.INSTANCE, 20 + (int)(venom * 20), 0));
				event.setAmount(residual);
			}
		}
	}
	
	@SubscribeEvent
	public void onPotionAdded(PotionAddedEvent event) {
		if(event.getPotionEffect().getPotion() == VenomEffect.INSTANCE) {
			if(event.getOldPotionEffect() != null) {
				if(event.getPotionEffect().getDuration() > event.getOldPotionEffect().getDuration()) {
					ObfuscationReflectionHelper.setPrivateValue(EffectInstance.class, event.getPotionEffect(), event.getPotionEffect().getAmplifier() + 1, Reflection.AMPLIFIER);
				}
			}
		}
	}
}
