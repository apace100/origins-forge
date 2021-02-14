package io.github.apace100.origins.power.cat;

import io.github.apace100.origins.power.Power;
import io.github.apace100.tags.ModBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WeakStoneBreakingPower extends Power {

	public WeakStoneBreakingPower(String name) {
		super(name);
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onDetermineBreakSpeed(PlayerEvent.BreakSpeed event) {
		BlockState blockState = event.getPlayer().world.getBlockState(event.getPos());
		if(ModBlockTags.NATURAL_STONE.contains(blockState.getBlock()) && isActive(event.getPlayer())) {
			if(event.getPlayer().isPotionActive(Effects.STRENGTH)) {
				return;
			}
			Hand hand = event.getPlayer().getActiveHand();
			if(hand == null) {
				hand = Hand.MAIN_HAND;
			}
			ItemStack item = event.getPlayer().getHeldItem(hand);
			if(ForgeHooks.isToolEffective(event.getPlayer().world, event.getPos(), item)) {
				int maxSurrounding = 3;
				int surrounding = 0;
				for(Direction d : Direction.values()) {
					if(ModBlockTags.NATURAL_STONE.contains(event.getPlayer().world.getBlockState(event.getPos().offset(d)).getBlock())) {
						surrounding++;
					}
				}
				if(surrounding > maxSurrounding) {
					event.setNewSpeed(0F);
				}
			} else {
				event.setNewSpeed(0F);
			}
		}
	}
}
