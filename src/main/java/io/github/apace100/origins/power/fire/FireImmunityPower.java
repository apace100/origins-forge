package io.github.apace100.origins.power.fire;

import io.github.apace100.origins.client.FireImmunityPowerClient;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.Powers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;

public class FireImmunityPower extends Power {
	
	public FireImmunityPower(String name) {
		super(name);
		this.setEventHandler();
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new FireImmunityPowerClient(this)));
	}

	@SubscribeEvent
	public void onLivingAttacked(LivingAttackEvent event) {
		if(event.getSource().isFireDamage()) {
			if(event.getEntityLiving() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity)event.getEntityLiving();
				if(Powers.FIRE_IMMUNITY.isActive(player)) {
					event.setCanceled(true);
				}
			}
		}
	}
}

