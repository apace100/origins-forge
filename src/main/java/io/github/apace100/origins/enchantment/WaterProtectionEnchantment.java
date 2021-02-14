package io.github.apace100.origins.enchantment;

import io.github.apace100.origins.OriginsMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.registries.ObjectHolder;

public class WaterProtectionEnchantment extends Enchantment {

	@ObjectHolder(OriginsMod.MODID + ":water_protection")
	public static Enchantment INSTANCE;
	
	public WaterProtectionEnchantment() {
		super(Enchantment.Rarity.RARE, EnchantmentType.ARMOR, new EquipmentSlotType[] {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET});
	}

	@Override
	public int getMinLevel() {
		return 1;
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	protected boolean canApplyTogether(Enchantment ench) {
		return !(ench instanceof ProtectionEnchantment) && !(ench instanceof WaterProtectionEnchantment);
	}

	
}
