package io.github.apace100.origins.block;

import io.github.apace100.origins.power.Powers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WebBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SpiderWebBlock extends WebBlock {
	
	public SpiderWebBlock() {
		super(Block.Properties.create(Material.WEB).doesNotBlockMovement().hardnessAndResistance(4.0F));
	}

	@Override
	public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity) {
		if(entity instanceof PlayerEntity) {
			if(Powers.SPIDERWEB.isActive((PlayerEntity)entity)) {
				return true; // Make cobweb act as ladder when player is spider
			}
		}
		return super.isLadder(state, world, pos, entity);
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if(entityIn instanceof PlayerEntity) {
			if(Powers.SPIDERWEB.isActive((PlayerEntity)entityIn)) {
				return; // Prevent Cobweb from slowing movement when player is spider
			}
		}
		super.onEntityCollision(state, worldIn, pos, entityIn);
	}

	@SubscribeEvent
	public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
		if(event.getState().getBlock() instanceof WebBlock) {
			if(Powers.SPIDERWEB.isActive(event.getPlayer())) {
				event.setNewSpeed(event.getOriginalSpeed() * 5f);
			}
		}
	}
}
