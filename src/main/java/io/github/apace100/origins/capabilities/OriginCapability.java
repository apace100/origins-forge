package io.github.apace100.origins.capabilities;

import io.github.apace100.origins.OriginsMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OriginCapability {

	@CapabilityInject(IOriginHolder.class)
	public static Capability<IOriginHolder> CAPABILITY = null;
	public static final ResourceLocation LOCATION = new ResourceLocation(OriginsMod.MODID, "origin");
	
	public static void register() {
		CapabilityManager.INSTANCE.register(IOriginHolder.class, new IStorage<IOriginHolder>()
        {

			@Override
			public INBT writeNBT(Capability<IOriginHolder> capability, IOriginHolder instance, Direction side) {
				return instance.serializeToNBT();
			}

			@Override
			public void readNBT(Capability<IOriginHolder> capability, IOriginHolder instance, Direction side, INBT nbt) {
				instance.deserializeFromNBT(nbt, null);
			}
        },
        () -> new OriginHolder());
	}
	
	@SubscribeEvent
	public static void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
		if(event.getObject() instanceof PlayerEntity) {
			event.addCapability(LOCATION, new ICapabilitySerializable<INBT>() {
				private IOriginHolder instance = CAPABILITY.getDefaultInstance();

				@SuppressWarnings("unchecked")
				@Override
				public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
					if(cap == CAPABILITY) {
						return (LazyOptional<T>)LazyOptional.of(() -> instance);
					}
					return LazyOptional.empty();
				}

				@Override
				public INBT serializeNBT() {
					return instance.serializeToNBT();
				}

				@Override
				public void deserializeNBT(INBT nbt) {
					instance.deserializeFromNBT(nbt, (PlayerEntity)event.getObject());
				}
				
			});
		}
	}
}
