package io.github.apace100.origins.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ServerProxy implements IProxy {

	@Override
	public World getClientWorld() {
		throw new IllegalStateException("Proxy method IProxy#getClientWorld is not available on the server.");
	}

	@Override
	public void setup() {

	}

	@Override
	public void clientSetup() {
		
	}

	@Override
	public void openOriginGui() {
		
	}

	@Override
	public PlayerEntity getClientPlayer() {
		throw new IllegalStateException("Should not call proxy#getClientPlayer when on the server.");
	}

}
