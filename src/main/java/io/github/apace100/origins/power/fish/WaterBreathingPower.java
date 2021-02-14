package io.github.apace100.origins.power.fish;

import io.github.apace100.origins.client.WaterBreathingPowerClient;
import io.github.apace100.origins.effect.AirBreathingEffect;
import io.github.apace100.origins.network.NetworkHelper;
import io.github.apace100.origins.power.Power;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;

public class WaterBreathingPower extends Power {

	public static final int MAX_AIR = 1200;

	public WaterBreathingPower(String registryName) {
		super(registryName);
		this.setTicking();
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new WaterBreathingPowerClient(this)));
	}

	@Override
	public void onTick(PlayerEntity player) {
		if(!player.world.isRemote) {
			CompoundNBT powerData = ((CompoundNBT)getPowerData(player));
			int air = getAir(powerData);
			if(player.areEyesInFluid(FluidTags.WATER)) {
				player.setAir(player.getMaxAir());
				if(air < MAX_AIR) {
					setAir(powerData, air + 3 > MAX_AIR ? MAX_AIR : air + 3);
					NetworkHelper.syncOrigin(player);
				}
			} else {
				if(!player.isPotionActive(AirBreathingEffect.INSTANCE) && !player.world.isRainingAt(player.getPosition())) {
					int i = EnchantmentHelper.getRespirationModifier(player);
					if(i > 0 && player.getRNG().nextInt(i + 1) > 0) {
					} else {
						air -= 1;
					}
					if(air <= -20) {
						player.attackEntityFrom(DamageSource.DRYOUT, 2.0F);
						player.playSound(SoundEvents.ENTITY_SQUID_HURT, 1f, 1f);
						air = 0;
					}
					setAir(powerData, air);
					NetworkHelper.syncOrigin(player);
				}
			}
		}

	}

	private void setAir(CompoundNBT nbt, int value) {
		nbt.putInt("Air", value);
	}

	private int getAir(CompoundNBT nbt) {
		return nbt.getInt("Air");
	}

	@Override
	public CompoundNBT createPowerData() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("Air", MAX_AIR);
		return nbt;
	}

}

