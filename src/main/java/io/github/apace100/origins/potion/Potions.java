package io.github.apace100.origins.potion;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.effect.AirBreathingEffect;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class Potions {
	
	@ObjectHolder(OriginsMod.MODID + ":air_breathing")
	public static Potion AIR_BREATHING;
	
	@ObjectHolder(OriginsMod.MODID + ":long_air_breathing")
	public static Potion LONG_AIR_BREATHING;
	
	public static void registerPotions(IForgeRegistry<Potion> registry) {
		registry.register(new Potion(new EffectInstance(AirBreathingEffect.INSTANCE, 3600)).setRegistryName("air_breathing"));
		registry.register(new Potion(new EffectInstance(AirBreathingEffect.INSTANCE, 7200)).setRegistryName("long_air_breathing"));
	}
	
	public static void registerRecipes() {
		PotionBrewing.addMix(net.minecraft.potion.Potions.WATER_BREATHING, Items.FERMENTED_SPIDER_EYE, AIR_BREATHING);
		PotionBrewing.addMix(net.minecraft.potion.Potions.LONG_WATER_BREATHING, Items.FERMENTED_SPIDER_EYE, LONG_AIR_BREATHING);
		PotionBrewing.addMix(AIR_BREATHING, Items.REDSTONE, LONG_AIR_BREATHING);
	}
}
