package io.github.apace100.origins.power.bird;

import io.github.apace100.origins.power.Power;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HighBedPower extends Power {

	public HighBedPower(String name) {
		super(name);
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onSleepCheck(SleepingLocationCheckEvent event) {
		if(isActive(event.getEntityLiving()) && event.getSleepingLocation().getY() < 86) {
			event.setResult(Result.DENY);
		}
	}
	
	@SubscribeEvent
	public void onSleepCheck(SleepingTimeCheckEvent event) {
		if(isActive(event.getEntityLiving()) && event.getSleepingLocation().isPresent() && event.getSleepingLocation().get().getY() < 86) {
			event.setResult(Result.DENY);
		}
	}
}
