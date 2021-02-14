package io.github.apace100.origins.power.ender;

import java.util.Random;

import io.github.apace100.origins.entity.SuperEnderPearlEntity;
import io.github.apace100.origins.power.UsablePower;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ThrowEnderPearlPower extends UsablePower {

	private Random random = new Random();

	public ThrowEnderPearlPower(String name) {
		super(name, 20);
	}

	@Override
	public void perform(PlayerEntity player) {
		player.world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		if (!player.world.isRemote) {
			EnderPearlEntity enderpearlentity = new SuperEnderPearlEntity(player.world, player);
			enderpearlentity.setItem(new ItemStack(Items.ENDER_PEARL));
			enderpearlentity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
			player.world.addEntity(enderpearlentity);
		}
	}

	@SubscribeEvent
	public void onLivingAttacked(LivingAttackEvent event) {
		if(event.getEntity() instanceof PlayerEntity) {
			if(event.getSource().getImmediateSource() instanceof EnderPearlEntity) {
				if(isActive((PlayerEntity)event.getEntity())) {
					event.setCanceled(true);
				}
			}
		}
	}
}

