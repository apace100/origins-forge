package io.github.apace100.origins.power;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class PotionPower extends Power {
	
	private Effect[] effectsToApply;
	
	public PotionPower(String name, Effect... potionEffects) {
		super(name);
		this.setTicking();
		this.effectsToApply = potionEffects;
	}

	@Override
	public void onTick(PlayerEntity player) {
		if(!player.world.isRemote) {
			if(this.areConditionsFulfilled(player)) {
				for(Effect e : effectsToApply) {
					if(player.isPotionActive(e) && player.getActivePotionEffect(e).getDuration() > 32000) {
						player.removePotionEffect(e);
					}
				}
			} else {
				for(Effect e : effectsToApply) {
					if(!player.isPotionActive(e)) {
						player.addPotionEffect(new EffectInstance(e, Integer.MAX_VALUE, 0, true, false, false));
					}
				}
			}
		}
	}
}
