package io.github.apace100.origins;

import io.github.apace100.origins.capabilities.IOriginHolder;
import io.github.apace100.origins.capabilities.OriginCapability;
import io.github.apace100.origins.config.Config;
import io.github.apace100.origins.network.ModPacketHandler;
import io.github.apace100.origins.network.NetworkHelper;
import io.github.apace100.origins.network.SyncOriginMessage;
import io.github.apace100.origins.origins.OriginManager;
import io.github.apace100.origins.origins.Origins;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;

public class OriginEventHandler {

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			LazyOptional<IOriginHolder> originHolder = player.getCapability(OriginCapability.CAPABILITY);
			if(!event.getWorld().isRemote) {
				// NetworkHelper.sendToAllTracking(, player)
				SyncOriginMessage msg = new SyncOriginMessage(player);
				
				originHolder.ifPresent(cap -> ModPacketHandler.INSTANCE.sendTo(msg, ((ServerPlayerEntity)player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT));
				originHolder.ifPresent(cap -> cap.onAdded(player));
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityTick(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			LazyOptional<IOriginHolder> originHolder = player.getCapability(OriginCapability.CAPABILITY);
			originHolder.ifPresent(cap -> cap.onTick(player));
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerDeath(LivingDeathEvent event) {
		boolean newOrigin = false;
		if(newOrigin) {
			if(event.getEntityLiving() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity)event.getEntityLiving();
				player.getCapability(OriginCapability.CAPABILITY).ifPresent(oh -> oh.setOrigin(Origins.EMPTY, player));
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		event.getOriginal().getCapability(OriginCapability.CAPABILITY).ifPresent(originalCap -> {
			event.getPlayer().getCapability(OriginCapability.CAPABILITY).ifPresent(newCap -> {
				boolean newOrigin = Config.NEW_ORIGIN_ON_DEATH.get();
				if(!newOrigin) {
					newCap.deserializeFromNBT(originalCap.serializeToNBT(), event.getPlayer());
					if(event.isWasDeath()) {
						newCap.onDeath(event.getPlayer());
					}
					NetworkHelper.syncOrigin(event.getPlayer());
				} else {
					newCap.setOrigin(Origins.EMPTY, event.getPlayer());
					NetworkHelper.syncOrigin(event.getPlayer());
					ModPacketHandler.INSTANCE.sendTo(OriginManager.MSG, ((ServerPlayerEntity)event.getPlayer()).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
				}
				
			});
		});
	}
}
