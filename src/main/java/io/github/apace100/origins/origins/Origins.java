package io.github.apace100.origins.origins;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.collect.ImmutableSet;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.power.Powers;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class Origins {
	protected static HashMap<ResourceLocation, Origin> REGISTRY = new HashMap<>();
	

	public static Origin EMPTY = new Origin(OriginsMod.MODID + ":empty", Powers.INVULNERABILITY).setUnchoosable();
	public static Origin HUMAN = new Origin(OriginsMod.MODID + ":human", Powers.POTION_EFFICIENCY).setDisplayItem(Items.PLAYER_HEAD);
	/*public static Origin BIRD = register(new Origin(Origins.MODID + ":bird", Powers.RUN_BOOST, Powers.SLOW_FALL, Powers.VEGETARIAN, Powers.POTION_INEFFICIENCY).setDisplayItem(Items.FEATHER));
	public static Origin ROCK = register(new Origin(Origins.MODID + ":rock", Powers.NATURAL_ARMOR, Powers.SLOWNESS, Powers.POTION_INEFFICIENCY).setDisplayItem(Items.QUARTZ));
	public static Origin FISH = register(new Origin(Origins.MODID + ":fish", Powers.WATER_BREATHING, Powers.WATER_VISION, Powers.WATER_DIGGING, Powers.WATER_SPEED, Powers.POTION_INEFFICIENCY).setDisplayItem(Items.COD));
	public static Origin FIRE = register(new Origin(Origins.MODID + ":fire", Powers.THROW_FIREBALL, Powers.FIRE_IMMUNITY, Powers.NO_RANGED, Powers.HURTFUL_WATER_POWER, Powers.POTION_INEFFICIENCY).setDisplayItem(Items.BLAZE_POWDER));
	public static Origin SPIDER = register(new Origin(Origins.MODID + ":spider", Powers.CLIMBING, Powers.POISON_DAMAGE, Powers.POISON_IMMUNITY, Powers.ONLY_MEAT, Powers.NO_CAKE, Powers.LESS_HP, Powers.POTION_INEFFICIENCY).setDisplayItem(Items.COBWEB));
	public static Origin ENDER = register(new Origin(Origins.MODID + ":ender", Powers.THROW_ENDER_PEARL, Powers.NO_PUMPKIN_PIE, Powers.HURTFUL_WATER_POWER, Powers.POTION_INEFFICIENCY).setDisplayItem(Items.ENDER_PEARL));
	public static Origin PHANTOM = register(new Origin(Origins.MODID + ":phantom", Powers.EXTRA_INVENTORY, Powers.POTION_INEFFICIENCY).setDisplayItem(Items.PHANTOM_MEMBRANE));
	*///public static Origin WITHER = register(new Origin(Origins.MODID + ":wither", Powers.WITHER_DAMAGE, Powers.WITHER_IMMUNITY, Powers.POTION_INEFFICIENCY).setDisplayItem(Items.WITHER_SKELETON_SKULL));
	
	public static Origin register(Origin origin) {
		ResourceLocation registryName = origin.getRegistryName();
		if(registryName == null) {
			throw new IllegalArgumentException("Someone tried to register an Origin without registry name.");
		}
		if(REGISTRY.containsKey(registryName)) {
			throw new IllegalArgumentException("An Origin with registry name " + registryName.toString() + " is already registered.");
		}
		REGISTRY.put(registryName, origin);
		return origin;
	}
	
	public static Origin getByName(String registryName) {
		return getByResourceLocation(new ResourceLocation(registryName));
	}
	
	public static Origin getByResourceLocation(ResourceLocation location) {
		if(!REGISTRY.containsKey(location)) {
			throw new IllegalArgumentException("Tried to get Origin by location at location " + location.toString() + ", but there is none.");
		}
		return REGISTRY.get(location);
	}
	
	public static Collection<Origin> getAll() {
		return ImmutableSet.copyOf(REGISTRY.values());
	}
	
	public static void clear() {
		REGISTRY.clear();
		Origin.nextOrder = 0;
		register(EMPTY);
		register(HUMAN);
	}
}
