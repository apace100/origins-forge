package io.github.apace100.origins.power.creeper;

import java.util.List;

import io.github.apace100.origins.network.NetworkHelper;
import io.github.apace100.origins.power.ChangingResourcePower;
import io.github.apace100.origins.power.Powers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion.Mode;

public class GunpowderResourcePower extends ChangingResourcePower {

	private static final int baseTicksPerChange = 8;
	private static final int fireTicksPerChangeReduction = 3;
	private static final int proximityTicksPerChangeReduction = 4;
	
	public GunpowderResourcePower(String name) {
		super(name, 0, 700, 700, baseTicksPerChange, -1);
	}

	@Override
	public void onTick(PlayerEntity player) {
		if(!player.isCreative()) {
			int ticksPerChange = baseTicksPerChange;
			if(player.isBurning()) {
				ticksPerChange -= fireTicksPerChangeReduction;
			}
			double range = 2;
			AxisAlignedBB bb = player.getBoundingBox();
			bb = bb.expand(range, 0.5, range);
			bb = bb.expand(-range, -0.5, -range);
			List<Entity> list = player.world.getEntitiesInAABBexcluding(player, bb, e -> (e instanceof PlayerEntity || e instanceof VillagerEntity) && e.getDistanceSq(player) <= range * range);
			if(list.size() > 0) {
				ticksPerChange -= proximityTicksPerChangeReduction;
			}
			this.ticksPerChange = ticksPerChange;
			super.onTick(player);
			if(this.getValue(player) <= 0) {
				Powers.GUNPOWDER_RESOURCE.changeValueInBars(player, 4F);
				NetworkHelper.syncOrigin(player);
				player.world.createExplosion(player, player.getPosX(), player.getPosY(), player.getPosZ(), 10F, Mode.BREAK);
			}
		}
	}
}
