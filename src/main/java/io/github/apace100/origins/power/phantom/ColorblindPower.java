package io.github.apace100.origins.power.phantom;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.client.ColorblindPowerClient;
import io.github.apace100.origins.power.Power;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;

public class ColorblindPower extends Power {

	public static boolean refreshShader = false;
	
	public ColorblindPower(String name) {
		super(name);
		this.setEventHandler();
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new ColorblindPowerClient(this)));
	}

	@Override
	public void onPowerAdded(PlayerEntity player) {
		if(player.world.isRemote) {
			if(OriginsMod.proxy.getClientPlayer() == player) {
				refreshShader = true;
			}
		}
	}

	@Override
	public void onPowerRemoved(PlayerEntity player) {
		if(player.world.isRemote) {
			if(OriginsMod.proxy.getClientPlayer() == player) {
				refreshShader = true;
			}
		}
	}

	
}