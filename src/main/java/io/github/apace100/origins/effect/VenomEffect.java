package io.github.apace100.origins.effect;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.apace100.origins.OriginsMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraftforge.registries.ObjectHolder;

public class VenomEffect extends Effect {

	@ObjectHolder(OriginsMod.MODID + ":venom")
	public static Effect INSTANCE;
	
	public VenomEffect(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
	}

	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
		entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, 1.0F);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int j = 20 >> amplifier;
		if (j > 0) {
			return duration % j == 0;
		} else {
			return true;
		}
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return Lists.newArrayList();
	}
	
	
}
