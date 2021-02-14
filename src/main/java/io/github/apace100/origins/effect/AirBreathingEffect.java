package io.github.apace100.origins.effect;

import io.github.apace100.origins.OriginsMod;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.registries.ObjectHolder;

public class AirBreathingEffect extends Effect {

	@ObjectHolder(OriginsMod.MODID + ":air_breathing")
	public static Effect INSTANCE;
	
	public AirBreathingEffect(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
	}
}
