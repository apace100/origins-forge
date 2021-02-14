package io.github.apace100.origins.power.golem;

import io.github.apace100.origins.client.BadSunVisionPowerClient;
import io.github.apace100.origins.power.Power;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;

public class BadSunVisionPower extends Power {

	public BadSunVisionPower(String name) {
		super(name);
		this.setEventHandler();
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new BadSunVisionPowerClient(this)));
	}

}
