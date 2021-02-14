package io.github.apace100.origins.power.spider;

import io.github.apace100.origins.block.TemporarySpiderWebBlock;
import io.github.apace100.origins.power.Power;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CobwebPower extends Power {

	public CobwebPower(String name) {
		super(name);
		this.setEventHandler();
	}
	
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		Entity ent = event.getSource().getTrueSource();
		if(ent == null) {
			return;
		}
		if(!ent.world.isRemote) {
			if(ent instanceof PlayerEntity) {
				if(isActive((PlayerEntity)ent)) {
					if(!ent.isCrouching()) {
						createTemporaryWeb(ent.world, event.getEntityLiving().getPosition());
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if(!event.getWorld().isRemote && isActive(event.getPlayer())) {
			ItemStack held = event.getPlayer().getHeldItem(event.getHand());
			if(held != null && held.getItem() == Items.STRING) {
				if(event.getPlayer().isCrouching()) {
					this.createPermanentWeb(event.getWorld(), event.getPos().offset(event.getFace()));
					event.setUseBlock(Result.DENY);
					event.setUseItem(Result.DENY);
					held.shrink(1);
				}
			}
		}
		
	}
	
	private void createTemporaryWeb(World world, BlockPos pos) {
		if(world.isAirBlock(pos)) {
			world.setBlockState(pos, TemporarySpiderWebBlock.INSTANCE.getDefaultState());
		}
	}
	
	private void createPermanentWeb(World world, BlockPos pos) {
		if(world.isAirBlock(pos)) {
			world.setBlockState(pos, Blocks.COBWEB.getDefaultState());
		}
	}
}
