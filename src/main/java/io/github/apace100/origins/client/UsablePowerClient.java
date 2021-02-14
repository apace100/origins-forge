package io.github.apace100.origins.client;

import io.github.apace100.origins.KeyBindings;
import io.github.apace100.origins.network.ModPacketHandler;
import io.github.apace100.origins.network.RightClickPowerMessage;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.UsablePower;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class UsablePowerClient extends PowerClient {

	public UsablePowerClient(Power wrap) {
		super(wrap);
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if(Minecraft.getInstance().currentScreen == null) {
			PlayerEntity player = Minecraft.getInstance().player;
			if(this.isActive(player) && KeyBindings.USE_POWER.isPressed()) {
				CompoundNBT data = this.getPowerData(player);
				int nextThrow = data.getInt("NextUse");
				if(player.world.getGameTime() >= nextThrow) {
					UsablePower up = (UsablePower)getPower();
					ModPacketHandler.INSTANCE.sendToServer(new RightClickPowerMessage(up));
					up.performClient(player);
					data.putLong("NextUse", player.world.getGameTime() + up.getCooldown());
				}
			}
		}
	}
}
