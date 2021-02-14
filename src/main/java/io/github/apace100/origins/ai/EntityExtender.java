package io.github.apace100.origins.ai;

import io.github.apace100.origins.power.Powers;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityExtender {
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof CreeperEntity) {
			CreeperEntity creeper = (CreeperEntity) event.getEntity();
			creeper.goalSelector.addGoal(3, new AvoidEntityGoal<>(creeper, PlayerEntity.class, Powers.SCARE_CREEPERS::isActive, 6.0F, 1.0D, 1.2D, EntityPredicates.CAN_AI_TARGET::test));
		} else
		if(event.getEntity() instanceof PlayerEntity) {
			((PlayerEntity)event.getEntity()).foodStats = new OriginsFoodStats(((PlayerEntity)event.getEntity()).foodStats);
		}
	}
}
