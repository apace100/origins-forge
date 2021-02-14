package io.github.apace100.origins.power.fish;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.Powers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WaterDiggingPower extends Power {
	
	public WaterDiggingPower(String registryName) {
		super(registryName);
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onDetermineBreakSpeed(PlayerEvent.BreakSpeed event) {
		if(Powers.WATER_DIGGING.isActive(event.getPlayer())) {
			if(event.getPlayer().areEyesInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(event.getPlayer())) {
				event.setNewSpeed(event.getNewSpeed() * 5f);
				if(!event.getPlayer().onGround) {
					event.setNewSpeed(event.getNewSpeed() * 5f);
				}
			}
		}
	}
}

