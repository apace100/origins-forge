package io.github.apace100.origins.power;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TeleportingWaterPower extends Power {
	
	private static Random random = new Random();
	
	public TeleportingWaterPower(String name) {
		super(name);
		this.setTicking();
	}

	@Override
	public void onTick(PlayerEntity player) {
		if(!player.world.isRemote) {
			if(player.isInWater() || (player.world.isRainingAt(player.getPosition()) && player.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty())) {
				teleportToDryInRange(player, 16, 8, !player.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty());
			}
		}
	}
	
	private void teleportToDryInRange(PlayerEntity player, int rangeHorizontal, int rangeVertical, boolean ignoreRain) {
		BlockPos center = player.getPosition();
		BlockPos posMax = center.add( rangeHorizontal / 2,  rangeVertical / 2,  rangeHorizontal / 2);
		BlockPos posMin = center.add(-rangeHorizontal / 2, -rangeVertical / 2, -rangeHorizontal / 2);
		AxisAlignedBB origBB = player.getBoundingBox();
		AxisAlignedBB bb = player.getBoundingBox();
		List<BlockPos> possibleTargets = new LinkedList<BlockPos>();
		for(int i = posMin.getX(); i <= posMax.getX(); i++) {
			for(int j = posMin.getY(); j <= posMax.getY(); j++) {
				for(int k = posMin.getZ(); k <= posMax.getZ(); k++) {
					BlockPos target = new BlockPos(i, j, k);
					bb = origBB.offset(target.subtract(player.getPosition()));
					if(!player.world.containsAnyLiquid(bb) && player.world.hasNoCollisions(bb) && player.world.getBlockState(target.down()).isSolid() && (!ignoreRain || !player.world.isRainingAt(target))) {
						possibleTargets.add(target);
					}
				}
			}
		}
		System.out.println("Possible targets: " + possibleTargets.size());
		BlockPos target = null;
		if(possibleTargets.size() > 0) {
			target = possibleTargets.get(random.nextInt(possibleTargets.size()));
		}
		if(target != null) {
			player.teleportKeepLoaded(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5);
		}
	}
}
