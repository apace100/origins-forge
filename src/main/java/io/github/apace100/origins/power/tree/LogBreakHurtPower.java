package io.github.apace100.origins.power.tree;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import io.github.apace100.origins.power.Power;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LogBreakHurtPower extends Power {

	private Random random = new Random();
	
	public LogBreakHurtPower(String name) {
		super(name);
		this.setEventHandler();
	}

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		if(!event.getWorld().isRemote()) {
			if(random.nextInt(3) == 0) {
				if(event.getState().getBlock().isIn(BlockTags.LOGS) && isActive(event.getPlayer())) {
					if(isTree(event.getWorld(), event.getPos(), Sets.newHashSet())) {
						event.getPlayer().attackEntityFrom(DamageSource.causePlayerDamage(event.getPlayer()), 1F);
					}
				}
			}
		}
	}
	
	private static final Direction[] DIRECTIONS = new Direction[] {Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN};
	
	private boolean isTree(IWorld iWorld, BlockPos pos, Set<BlockPos> checked) {
		if(checked.contains(pos)) {
			return false;
		}
		if(iWorld.getBlockState(pos).getBlock().isIn(BlockTags.LEAVES)) {
			return true;
		}
		if(iWorld.getBlockState(pos).getBlock().isIn(BlockTags.LOGS)) {
			checked.add(pos);
			for(int i = 0; i < DIRECTIONS.length; i++) {
				if(isTree(iWorld, pos.offset(DIRECTIONS[i]), checked)) {
					return true;
				}
			}
		}
		return false;
	}
}
