package io.github.apace100.origins.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy {

	World getClientWorld();

	void setup();

	void clientSetup();
	
	void openOriginGui();
	
	PlayerEntity getClientPlayer();
}
