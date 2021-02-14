package io.github.apace100.origins.power.spider;

import java.util.LinkedList;
import java.util.List;

import io.github.apace100.origins.network.ModPacketHandler;
import io.github.apace100.origins.network.PlayerClimbMessage;
import io.github.apace100.origins.power.Power;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.PooledMutable;
import net.minecraft.util.math.Vec3d;

public class ClimbPower extends Power {

	public ClimbPower(String name) {
		super(name);
		this.setTicking();
	}

	@Override
	public void onTick(PlayerEntity player) {
		if(player.world.isRemote) {
			CompoundNBT data = getPowerData(player);
			if(player.collidedHorizontally) {
				Vec3d vec = player.getMotion();
				player.setMotion(vec.x, player.isCrouching() ? 0 : 0.15, vec.z);
				player.fallDistance = 0;
				player.velocityChanged = true;
				data.putBoolean("WasClimbing", true);
				ModPacketHandler.INSTANCE.sendToServer(new PlayerClimbMessage());
			} else {
				if(data.getBoolean("WasClimbing")) {
					if(player.isCrouching() && canClimbHere(player)) {
						Vec3d vec = player.getMotion();
						player.setMotion(vec.x, 0, vec.z);
						player.fallDistance = 0;
						player.velocityChanged = true;
						ModPacketHandler.INSTANCE.sendToServer(new PlayerClimbMessage());
					} else {
						data.putBoolean("WasClimbing", false);
					}
				}
			}
		}
	}
	
	private boolean canClimbHere(PlayerEntity player) {
		for(BlockPos p : findCollidableBlockPos(player)) {
			BlockState state = player.world.getBlockState(p);
			if(state.isSolid()) {
				return true;
			}
		}
		return player.world.isMaterialInBB(player.getBoundingBox(), Material.WEB);
	}

	@SuppressWarnings("deprecation")
	private List<BlockPos> findCollidableBlockPos(PlayerEntity player) {
		AxisAlignedBB bb = player.getBoundingBox();
		List<BlockPos> blocks = new LinkedList<>();
		try (
			PooledMutable b0 = PooledMutable.retain(bb.minX - 0.002D, bb.minY, bb.minZ - 0.002D);
			PooledMutable b1 = PooledMutable.retain(bb.maxX + 0.002D, bb.maxY, bb.maxZ + 0.002D);
				) {
			if (player.world.isAreaLoaded(b0, b1)) {
				for(int i = b0.getX(); i <= b1.getX(); ++i) {
					for(int j = b0.getY(); j <= b1.getY(); ++j) {
						for(int k = b0.getZ(); k <= b1.getZ(); ++k) {

							blocks.add(new BlockPos(i, j, k));
						}
					}
				}
			}
		}
		return blocks;
	}

	@Override
	public CompoundNBT createPowerData() {
		CompoundNBT nbt = super.createPowerData();
		nbt.putBoolean("WasClimbing", false);
		return nbt;
	}
}
