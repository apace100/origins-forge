package io.github.apace100.origins.power;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Stream;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.api.RegisterPowerIMC;
import io.github.apace100.origins.power.bird.HighBedPower;
import io.github.apace100.origins.power.bird.JumpBoostPower;
import io.github.apace100.origins.power.bird.SlowFallPower;
import io.github.apace100.origins.power.cat.NoFallDamagePower;
import io.github.apace100.origins.power.cat.ScareCreepersPower;
import io.github.apace100.origins.power.cat.WeakStoneBreakingPower;
import io.github.apace100.origins.power.common.HurtfulWaterPower;
import io.github.apace100.origins.power.common.MultiplyPotionDurationPower;
import io.github.apace100.origins.power.creeper.ExplosionPower;
import io.github.apace100.origins.power.creeper.GunpowderResourcePower;
import io.github.apace100.origins.power.ender.ThrowEnderPearlPower;
import io.github.apace100.origins.power.fire.FireImmunityPower;
import io.github.apace100.origins.power.fire.NoMeleeDamagePower;
import io.github.apace100.origins.power.fire.ThrowFireballPower;
import io.github.apace100.origins.power.fish.WaterBreathingPower;
import io.github.apace100.origins.power.fish.WaterDiggingPower;
import io.github.apace100.origins.power.fish.WaterVisionPower;
import io.github.apace100.origins.power.golem.BadSunVisionPower;
import io.github.apace100.origins.power.golem.ExtraInventoryPower;
import io.github.apace100.origins.power.phantom.BurnInDaylightPower;
import io.github.apace100.origins.power.phantom.ColorblindPower;
import io.github.apace100.origins.power.phantom.InvisibilityPower;
import io.github.apace100.origins.power.phantom.LifestealPower;
import io.github.apace100.origins.power.phantom.NoAttacksPower;
import io.github.apace100.origins.power.spider.ClimbPower;
import io.github.apace100.origins.power.spider.CobwebImmunityPower;
import io.github.apace100.origins.power.spider.CobwebPower;
import io.github.apace100.origins.power.spider.InflictPoisonPower;
import io.github.apace100.origins.power.tree.BurnForeverPower;
import io.github.apace100.origins.power.tree.ComposterPower;
import io.github.apace100.origins.power.tree.LogBreakHurtPower;
import io.github.apace100.origins.power.tree.SunFoodPower;
import io.github.apace100.origins.power.tree.XPMultiplierPower;
import io.github.apace100.tags.ModItemTags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.InterModComms;

public class Powers {
	private static HashMap<ResourceLocation, Power> REGISTRY = new HashMap<>();

	public static Power INVULNERABILITY = register(new InvulnerabilityPower(OriginsMod.MODID + ":invulnerability").setHidden());
	
	// Common
	public static Power POTION_EFFICIENCY = register(new MultiplyPotionDurationPower(OriginsMod.MODID + ":potion_efficiency", 2));
	
	// Fish
	public static Power WATER_BREATHING = register(new WaterBreathingPower(OriginsMod.MODID + ":water_breathing"));
	public static Power WATER_VISION = register(new WaterVisionPower(OriginsMod.MODID + ":water_vision"));
	public static Power WATER_DIGGING = register(new WaterDiggingPower(OriginsMod.MODID + ":water_digging"));
	private static UUID WATER_SPEED_MOD = UUID.fromString("02632AF3-B6E8-481E-9E21-F481F6EC14FE");
	public static Power WATER_SPEED = register(new AttributeModifierPower(OriginsMod.MODID + ":water_speed").addAttributeModifier(LivingEntity.SWIM_SPEED, new AttributeModifier(WATER_SPEED_MOD, "Water speed from power", 0.7, Operation.MULTIPLY_TOTAL)));
	
	// Fire
	public static Power THROW_FIREBALL = register(new ThrowFireballPower(OriginsMod.MODID + ":throw_fireball"));
	public static Power FIRE_IMMUNITY = register(new FireImmunityPower(OriginsMod.MODID + ":fire_immunity"));
	public static Power NO_RANGED = register(new PreventItemUsePower(OriginsMod.MODID + ":no_ranged", ModItemTags.RANGED_WEAPONS));
	public static Power HURTFUL_WATER_POWER = register(new HurtfulWaterPower(OriginsMod.MODID + ":hurtful_water"));
	public static Power NO_MELEE_DAMAGE = register(new NoMeleeDamagePower(OriginsMod.MODID + ":no_melee_damage"));
	
	// Ender
	public static Power THROW_ENDER_PEARL = register(new ThrowEnderPearlPower(OriginsMod.MODID + ":throw_ender_pearl"));
	public static Power NO_PUMPKIN_PIE = register(new PreventItemUsePower(OriginsMod.MODID + ":no_pumpkin_pie", Items.PUMPKIN_PIE));
	
	// Bird
	public static Power JUMP_BOOST = register(new JumpBoostPower(OriginsMod.MODID + ":jump_boost", 1.5F).addCondition(PlayerEntity::isSprinting));
	private static UUID RUN_BOOST_MOD = UUID.fromString("F4C56C55-D5F1-4BC1-B661-A0409DE255A5");
	public static Power RUN_BOOST = register(new AttributeModifierPower(OriginsMod.MODID + ":run_boost").addAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, new AttributeModifier(RUN_BOOST_MOD, "Move speed from power", 0.1, Operation.MULTIPLY_BASE)));
	public static Power SLOW_FALL = register(new SlowFallPower(OriginsMod.MODID + ":slow_fall", 0.1));
	public static Power VEGETARIAN = register(new PreventItemUsePower(OriginsMod.MODID + ":vegetarian", ModItemTags.MEAT));
	public static Power HIGH_BED = register(new HighBedPower(OriginsMod.MODID + ":high_bed"));
	
	// Phantom
	public static InvisibilityPower INVISIBILITY = (InvisibilityPower)register(new InvisibilityPower(OriginsMod.MODID + ":invisibility"));
	public static Power NO_ATTACKS = register(new NoAttacksPower(OriginsMod.MODID + ":no_attacks"));
	public static Power BURN_IN_DAYLIGHT = register(new BurnInDaylightPower(OriginsMod.MODID + ":burn_in_daylight").addCondition(pe -> !INVISIBILITY.isInvisible(pe)));
	public static Power LIFESTEAL = register(new LifestealPower(OriginsMod.MODID + ":lifesteal", 0.5F));
	public static Power COLORBLIND = register(new ColorblindPower(OriginsMod.MODID + ":colorblind"));
	public static Power NIGHT_VISION = register(new PotionPower(OriginsMod.MODID + ":night_vision", Effects.NIGHT_VISION).addCondition(pe -> pe.areEyesInFluid(FluidTags.WATER)));
	public static Power NO_FOOD_REGEN = register(new Power(OriginsMod.MODID + ":no_food_regen"));
	
	// Spider
	public static Power CLIMBING = register(new ClimbPower(OriginsMod.MODID + ":climbing"));
	public static Power ONLY_MEAT = register(new PreventItemUsePower(OriginsMod.MODID + ":only_meat", stack -> stack.isFood() && !stack.getItem().isIn(ModItemTags.MEAT)));
	public static Power NO_CAKE = register(new PreventBlockUsePower(OriginsMod.MODID + ":no_cake", Blocks.CAKE).setHidden());
	public static Power POISON_DAMAGE = register(new VenomPower(OriginsMod.MODID + ":poison_damage", 1F, 1F));
	public static Power POISON_IMMUNITY = register(new PotionImmunityPower(OriginsMod.MODID + ":poison_immunity", Effects.POISON));
	public static Power SPIDERWEB = register(new CobwebPower(OriginsMod.MODID + ":spiderweb"));
	private static UUID LESS_HP_MOD = UUID.fromString("D0CD8CE6-8C53-4407-B78C-8816E8707688");
	public static Power LESS_HP = register(new AttributeModifierPower(OriginsMod.MODID + ":less_hp").addAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, new AttributeModifier(LESS_HP_MOD, "Less hp from power", -4, Operation.ADDITION)));
	
	// Rock
	public static Power BAD_SUN_VISION = register(new BadSunVisionPower(OriginsMod.MODID + ":bad_sun_vision"));
	private static UUID NATURAL_ARMOR_MOD = UUID.fromString("66BDFE9A-D467-4F44-8FB2-7EA8483C3303");
	public static Power NATURAL_ARMOR = register(new AttributeModifierPower(OriginsMod.MODID + ":natural_armor").addAttributeModifier(SharedMonsterAttributes.ARMOR, new AttributeModifier(NATURAL_ARMOR_MOD, "More armor from power", 10, Operation.ADDITION)));
	private static UUID SLOWNESS_MOD = UUID.fromString("96DC9F7F-8C1B-419F-895C-1301604AF047");
	public static Power SLOWNESS = register(new AttributeModifierPower(OriginsMod.MODID + ":slowness").addAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, new AttributeModifier(SLOWNESS_MOD, "Speed penalty from power", -0.3, Operation.MULTIPLY_BASE)).addCondition(player -> player.getFoodStats().getFoodLevel() < 16));
	public static Power NO_SHIELD = register(new PreventItemUsePower(OriginsMod.MODID + ":no_shield", Items.SHIELD));
	public static Power EXTRA_INVENTORY = register(new ExtraInventoryPower(OriginsMod.MODID + ":extra_inventory"));
	
	// Tree
	public static Power BURN_FOREVER = register(new BurnForeverPower(OriginsMod.MODID + ":burn_forever"));
	public static Power HURT_ON_LOG_BREAK = register(new LogBreakHurtPower(OriginsMod.MODID + ":hurt_on_log_break"));
	public static Power LUCKY_COMPOSTER = register(new ComposterPower(OriginsMod.MODID + ":lucky_composter"));
	public static Power MORE_XP = register(new XPMultiplierPower(OriginsMod.MODID + ":more_xp", 1.8F));
	public static Power PHOTOSYNTHESIS = register(new SunFoodPower(OriginsMod.MODID + ":photosynthesis"));
		
	// Cat
	public static Power SCARE_CREEPERS = register(new ScareCreepersPower(OriginsMod.MODID + ":scare_creepers"));
	public static Power NO_FALL_DAMAGE = register(new NoFallDamagePower(OriginsMod.MODID + ":no_fall_damage"));
	private static UUID NINE_LIVES_MOD = UUID.fromString("A0AE00AB-DD41-4D28-BD70-3F6FA3B6B19B");
	public static Power NINE_LIVES = register(new AttributeModifierPower(OriginsMod.MODID + ":nine_lives").addAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, new AttributeModifier(NINE_LIVES_MOD, "Less hp from power", -2, Operation.ADDITION)));
	public static Power WEAK_STONE_BREAKING = register(new WeakStoneBreakingPower(OriginsMod.MODID + ":weak_stone_breaking"));
	
	// Creeper
	public static ResourcePower GUNPOWDER_RESOURCE = (ResourcePower) register(new GunpowderResourcePower(OriginsMod.MODID + ":gunpowder_resource"));
	public static Power EXPLOSION = register(new ExplosionPower(OriginsMod.MODID + ":explosion"));
	
	// Unused or Not Functional
	public static Power COBWEB_IMMUNITY = register(new CobwebImmunityPower(OriginsMod.MODID + ":cobweb_immunity").setHidden());
	public static Power TP_IN_WATER = register(new TeleportingWaterPower(OriginsMod.MODID + ":tp_in_water"));
	public static Power INFLICT_POISON = register(new InflictPoisonPower(OriginsMod.MODID + ":inflict_poison"));
	public static Power NO_MILK = register(new PreventItemUsePower(OriginsMod.MODID + ":no_milk", Items.MILK_BUCKET).setHidden());
	
	public static Power register(Power power) {
		ResourceLocation registryName = power.getRegistryName();
		if(registryName == null) {
			throw new IllegalArgumentException("Someone tried to register a Power without registry name.");
		}
		if(REGISTRY.containsKey(registryName)) {
			throw new IllegalArgumentException("A Power with registry name " + registryName.toString() + " is already registered.");
		}
		REGISTRY.put(registryName, power);
		return power;
	}
	
	public static Power getByName(String registryName) {
		return getByResourceLocation(new ResourceLocation(registryName));
	}
	
	public static Power getByResourceLocation(ResourceLocation location) {
		if(!REGISTRY.containsKey(location)) {
			throw new IllegalArgumentException("Tried to get Power by location at location " + location.toString() + ", but there is none.");
		}
		return REGISTRY.get(location);
	}
	
	public static void processRegistryIMC(Stream<InterModComms.IMCMessage> stream) {
		stream.filter(msg -> msg.getMessageSupplier().get() instanceof RegisterPowerIMC)
			.map(msg -> (RegisterPowerIMC)msg.getMessageSupplier().get())
			.forEach(Powers::registerPowerFromIMC);
	}
	
	private static void registerPowerFromIMC(RegisterPowerIMC imc) {
		try {
			register(imc.getPower());
		} catch(IllegalArgumentException e) {
			System.err.println("Failing to register power from IMC: " + e.getMessage());
		}
	}
}
