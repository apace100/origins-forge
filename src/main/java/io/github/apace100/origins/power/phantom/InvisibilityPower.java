package io.github.apace100.origins.power.phantom;

import io.github.apace100.origins.client.InvisibilityPowerClient;
import io.github.apace100.origins.network.NetworkHelper;
import io.github.apace100.origins.power.UsablePower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;

public class InvisibilityPower extends UsablePower {

	protected static final int EXHAUSTION_COOLDOWN = 120;
	
	public InvisibilityPower(String name) {
		super(name, 20);
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new InvisibilityPowerClient(this)));
		this.setTicking();
	}
	
	@Override
	public void onTick(PlayerEntity player) {
		FoodStats food = player.getFoodStats();
		if(isInvisible(player)) {
			if(!player.world.isRemote) {
				CompoundNBT powerData = ((CompoundNBT)getPowerData(player));
				int cd = getCooldown(powerData);
				if(cd <= 0) {
					food.addExhaustion(2F);
					setCooldown(powerData, EXHAUSTION_COOLDOWN);
				} else {
					setCooldown(powerData, cd - 1);
				}
				if(food.getFoodLevel() <= 6) {
					perform(player);
				} else {
					player.setInvisible(true);
				}
			} else
			if(food.getFoodLevel() <= 6) {
				perform(player);
			}
		}
	}
	
	private void setCooldown(CompoundNBT nbt, int value) {
		nbt.putInt("ExhaustionCooldown", value);
	}

	private int getCooldown(CompoundNBT nbt) {
		return nbt.getInt("ExhaustionCooldown");
	}
	
	@Override
	public CompoundNBT createPowerData() {
		CompoundNBT nbt = super.createPowerData();
		nbt.putBoolean("Active", false);
		nbt.putInt("ExhaustionCooldown", EXHAUSTION_COOLDOWN);
		return nbt;
	}
	
	@SubscribeEvent
	public void onSetTarget(LivingSetAttackTargetEvent event) {
		if(!event.getEntity().world.isRemote && event.getEntity() instanceof MobEntity) {
			MobEntity mob = (MobEntity)event.getEntityLiving();
			if(isInvisible(event.getTarget())) {
				mob.setAttackTarget(null);
			}
		}
	}

	public boolean isInvisible(LivingEntity entity) {
		return entity instanceof PlayerEntity && isInvisible((PlayerEntity)entity);
	}
	
	public boolean isInvisible(PlayerEntity player) {
		return this.isActive(player) && this.getPowerData(player).getBoolean("Active");
	}

	@Override
	public void perform(PlayerEntity player) {
		if(player.getFoodStats().getFoodLevel() > 6) {
			CompoundNBT data = this.getPowerData(player);
			boolean isActive = data.getBoolean("Active");
			isActive = !isActive;
			if(!isActive) {
				player.setInvisible(false);
			}
			player.playSound(SoundEvents.ENTITY_PHANTOM_SWOOP, 1F, 1F);
			data.putBoolean("Active", isActive);
			NetworkHelper.syncOrigin(player);
		}
	}
}
