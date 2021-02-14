package io.github.apace100.origins.power;

import io.github.apace100.origins.client.ResourcePowerClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;

public class ResourcePower extends Power {

	public static boolean refreshShader = false;
	
	private int maxValue;
	private int startValue;
	
	private int index;
	
	private static final int BARS = 7;
	
	public ResourcePower(String name, int barIndex, int maxValue, int startValue) {
		super(name);
		this.maxValue = maxValue;
		this.index = barIndex;
		this.startValue = startValue;
		this.setEventHandler();
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new ResourcePowerClient(this)));
	}
	
	public int getIndex() {
		return index;
	}
	
	public float getFillInBars(PlayerEntity player) {
		return getFill(player) * BARS;
	}

	public float getFill(PlayerEntity player) {
		return (float)getValue(player) / (float)maxValue;
	}

	public void setValue(PlayerEntity player, int value) {
		CompoundNBT nbt = ((CompoundNBT)getPowerData(player));
		nbt.putInt("Value", value); 
	}

	public int getValue(PlayerEntity player) {
		CompoundNBT nbt = ((CompoundNBT)getPowerData(player));
		return nbt.getInt("Value");
	}
	
	public int changeValue(PlayerEntity player, int change) {
		CompoundNBT nbt = ((CompoundNBT)getPowerData(player));
		int current = nbt.getInt("Value");
		int newValue = current + change;
		if(newValue > maxValue) newValue = maxValue;
		if(newValue < 0) newValue = 0;
		nbt.putInt("Value", newValue);
		int delta = newValue - current;
		int leftover = change - delta;
		return leftover;
	}
	
	public float changeValueInBars(PlayerEntity player, float changeInBars) {
		int change = (int)(changeInBars / BARS * maxValue);
		return (changeValue(player, change) / (float)maxValue) * BARS;
	}

	@Override
	public CompoundNBT createPowerData() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("Value", startValue);
		return nbt;
	}

}