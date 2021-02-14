package io.github.apace100.origins.power.tree;

import io.github.apace100.origins.power.Power;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class XPMultiplierPower extends Power {

	private float multiplier;
	
	public XPMultiplierPower(String name, float multiplier) {
		super(name);
		this.multiplier = multiplier;
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onGainXP(PlayerXpEvent.XpChange event) {
		if(!event.getPlayer().world.isRemote) {
			if(event.getAmount() > 0) {
				if(isActive(event.getPlayer())) {
					event.setAmount((int)(event.getAmount() * this.multiplier));
				}
			}
		}
	}
}
