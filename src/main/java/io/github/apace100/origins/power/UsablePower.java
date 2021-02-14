package io.github.apace100.origins.power;

import io.github.apace100.origins.client.UsablePowerClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;

public abstract class UsablePower extends Power {
	
	protected int cooldown = 20;
	
	public UsablePower(String name, int cooldown) {
		super(name);
		this.cooldown = cooldown;
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new UsablePowerClient(this)));
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
	public abstract void perform(PlayerEntity player);
	
	public void performClient(PlayerEntity player) { };
	
	@Override
	public CompoundNBT createPowerData() {
		CompoundNBT nbt = super.createPowerData();
		nbt.putInt("NextUse", 0);
		return nbt;
	}
}
