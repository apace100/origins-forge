package io.github.apace100.origins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.apace100.origins.ai.EntityExtender;
import io.github.apace100.origins.api.OriginsAPI;
import io.github.apace100.origins.block.SpiderWebBlock;
import io.github.apace100.origins.block.TemporarySpiderWebBlock;
import io.github.apace100.origins.capabilities.OriginCapability;
import io.github.apace100.origins.command.OriginCommand;
import io.github.apace100.origins.config.Config;
import io.github.apace100.origins.effect.AirBreathingEffect;
import io.github.apace100.origins.effect.VenomEffect;
import io.github.apace100.origins.enchantment.WaterProtectionEnchantment;
import io.github.apace100.origins.entity.SuperEnderPearlEntity;
import io.github.apace100.origins.network.ModPacketHandler;
import io.github.apace100.origins.origins.OriginManager;
import io.github.apace100.origins.potion.Potions;
import io.github.apace100.origins.power.Powers;
import io.github.apace100.origins.setup.ClientProxy;
import io.github.apace100.origins.setup.IProxy;
import io.github.apace100.origins.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.IForgeRegistry;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OriginsMod.MODID)
public class OriginsMod {
    
	public static final String MODID = "origins";
	public static final String NAME = "Origins";
	public static final String VERSION = "1.0";
	
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
	
	public static OriginManager originManager;
	
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

    public OriginsMod() {
    	ModLoadingContext.get().registerConfig(Type.COMMON, Config.COMMON_CONFIG);
    	
    	final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    	bus.addListener(this::setup);
    	bus.addListener(this::clientSetup);
    	bus.addListener(this::processIMC);
    	MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
    	
    	Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("origins-common.toml"));
    }
    
    private void serverStarting(final FMLServerAboutToStartEvent event) {
    	IReloadableResourceManager resMngr = event.getServer().getResourceManager();
    	if(originManager != null) {
    		MinecraftForge.EVENT_BUS.unregister(originManager);
    	}
    	resMngr.addReloadListener(originManager = new OriginManager());
    	MinecraftForge.EVENT_BUS.register(originManager);
    	
    	OriginCommand.register(event.getServer().getCommandManager().getDispatcher());
    }

    private void setup(final FMLCommonSetupEvent event) {
        OriginCapability.register();
        ModPacketHandler.registerMessages();
        Potions.registerRecipes();
        MinecraftForge.EVENT_BUS.register(new OriginEventHandler());
        MinecraftForge.EVENT_BUS.register(new EntityExtender());
        proxy.setup();
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
    	DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> proxy.clientSetup());
    }
    
    private void processIMC(final InterModProcessEvent event) {
    	 Powers.processRegistryIMC(event.getIMCStream(OriginsAPI.IMC.REGISTER_POWER::equals));
    	 
    }
    
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        
    	@SubscribeEvent
        public static void onBlockRegistry(final RegistryEvent.Register<Block> event) {
            IForgeRegistry<Block> reg = event.getRegistry();
            reg.register(new SpiderWebBlock().setRegistryName("minecraft:cobweb"));
            reg.register(new TemporarySpiderWebBlock().setRegistryName("spiderweb"));
        }
    	
        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            IForgeRegistry<Item> reg = itemRegistryEvent.getRegistry();
            reg.register((new Item(new Item.Properties().group(ItemGroup.MISC) ) {

				@Override
				public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
					ItemStack itemstack = playerIn.getHeldItem(handIn);
					if(worldIn.isRemote) {
						OriginsMod.proxy.openOriginGui();
					} else {
						itemstack.shrink(1);
					}
					return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
				}
            	
            }).setRegistryName("orb_of_origin"));
        }

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event) {
        	event.getRegistry().register(
        			EntityType.Builder.<SuperEnderPearlEntity>create(SuperEnderPearlEntity::new, EntityClassification.MISC)
        			.build("super_ender_pearl").setRegistryName(MODID, "super_ender_pearl"));
        }

        @SubscribeEvent
        public static void onEnchantmentRegistry(final RegistryEvent.Register<Enchantment> event) {
        	event.getRegistry().register(new WaterProtectionEnchantment().setRegistryName("water_protection"));
        }

        @SubscribeEvent
        public static void onEffectRegistry(final RegistryEvent.Register<Effect> event) {
        	event.getRegistry().register(new VenomEffect(EffectType.HARMFUL, 0x00FF00).setRegistryName("venom"));
        	event.getRegistry().register(new AirBreathingEffect(EffectType.BENEFICIAL, 0x0000FF).setRegistryName("air_breathing"));
        }
        
        @SubscribeEvent
        public static void onPotionRegistry(final RegistryEvent.Register<Potion> event) {
        	Potions.registerPotions(event.getRegistry());
        }
    }
}
