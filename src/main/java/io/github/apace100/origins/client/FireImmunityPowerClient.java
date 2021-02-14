package io.github.apace100.origins.client;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.Powers;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FireImmunityPowerClient extends PowerClient {
	
	public FireImmunityPowerClient(Power wrap) {
		super(wrap);
	}

	@SubscribeEvent
	public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
		if(event.getOverlayType() == OverlayType.FIRE) {
			if(Powers.FIRE_IMMUNITY.isActive(event.getPlayer())) {
				event.setCanceled(true);
			}
		}
	}
}
