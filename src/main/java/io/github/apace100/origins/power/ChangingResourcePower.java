package io.github.apace100.origins.power;

import io.github.apace100.origins.network.NetworkHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class ChangingResourcePower extends ResourcePower {

	protected int ticksPerChange;
	protected int changeAmount;
	
	public ChangingResourcePower(String name, int barIndex, int maxValue, int startValue, int ticksPerChange, int changeAmount) {
		super(name, barIndex, maxValue, startValue);
		this.ticksPerChange = ticksPerChange;
		this.changeAmount = changeAmount;
		this.setTicking();
	}
	
	@Override
	public void onTick(PlayerEntity player) {
		if(!player.world.isRemote) {
			CompoundNBT powerData = ((CompoundNBT)getPowerData(player));
			int ticks = getTicks(powerData);
			if(this.areConditionsFulfilled(player)) {
				int newTicks = ticks - 1;
				if(newTicks <= 0) {
					this.setValue(player, this.getValue(player) + changeAmount);
					newTicks = ticksPerChange;
				}
				setTicks(powerData, newTicks);
				NetworkHelper.syncOrigin(player);
			}
		}

	}

	private void setTicks(CompoundNBT nbt, int value) {
		nbt.putInt("TicksToNextChange", value);
	}

	private int getTicks(CompoundNBT nbt) {
		return nbt.getInt("TicksToNextChange");
	}

	@Override
	public CompoundNBT createPowerData() {
		CompoundNBT nbt = super.createPowerData();
		nbt.putInt("TicksToNextChange", ticksPerChange);
		return nbt;
	}

	
}
