package io.github.apace100.origins.power;

import io.github.apace100.origins.network.ModPacketHandler;
import io.github.apace100.origins.network.RightClickPowerMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class RightClickPower extends Power {
	
	protected int cooldown = 20;
	
	public RightClickPower(String name, int cooldown) {
		super(name);
		this.cooldown = cooldown;
		this.setEventHandler();
	}
	
	public abstract void performRightClick(PlayerEntity player);
	
	public void performRightClickClient(PlayerEntity player) { };
	
	@Override
	public CompoundNBT createPowerData() {
		CompoundNBT nbt = super.createPowerData();
		nbt.putInt("NextUse", 0);
		return nbt;
	}

	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent.RightClickEmpty event) {
		PlayerEntity player = event.getPlayer();
		if(player.world.isRemote) {
			if(this.isActive(player)) {
				if(event.getHand() == Hand.MAIN_HAND) {
					CompoundNBT data = this.getPowerData(player);
					int nextThrow = data.getInt("NextUse");
					if(player.world.getGameTime() >= nextThrow) {
						ModPacketHandler.INSTANCE.sendToServer(new RightClickPowerMessage(this));
						performRightClickClient(player);
						data.putLong("NextUse", player.world.getGameTime() + this.cooldown);
					}
				}
			}
		}
		
	}
}
