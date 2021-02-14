package io.github.apace100.origins.power.tree;

import io.github.apace100.origins.power.Power;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ComposterPower extends Power {

	public ComposterPower(String name) {
		super(name);
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		PlayerEntity player = event.getPlayer();
		BlockPos pos = event.getPos();
		BlockState state = world.getBlockState(pos);
		if(!world.isRemote && state.getBlock() instanceof ComposterBlock && isActive(player)) {
			int i = state.get(ComposterBlock.LEVEL);
			if(i < 7) {
				ItemStack stack = player.getHeldItem(event.getHand());
				if(ComposterBlock.CHANCES.containsKey(stack.getItem())) {
					world.setBlockState(pos, state.with(ComposterBlock.LEVEL, i + 1));
					world.playEvent(1500, pos, 1);
		            if (!player.abilities.isCreativeMode) {
		               stack.shrink(1);
		            }
		            event.setCancellationResult(ActionResultType.SUCCESS);
					event.setCanceled(true);
				}
			}
		}
		
	}
}
