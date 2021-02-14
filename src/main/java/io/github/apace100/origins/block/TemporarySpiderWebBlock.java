package io.github.apace100.origins.block;

import java.util.Random;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.power.Powers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WebBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber
public class TemporarySpiderWebBlock extends WebBlock {

	@ObjectHolder(OriginsMod.MODID + ":spiderweb")
	public static TemporarySpiderWebBlock INSTANCE;
	
	public TemporarySpiderWebBlock() {
		super(Block.Properties.create(Material.WEB).doesNotBlockMovement().hardnessAndResistance(4.0F));
	}

	
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if(!worldIn.isRemote) {
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}


	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		worldIn.getPendingBlockTicks().scheduleTick(pos, this, 20);
		super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
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
		if(event.getState().getBlock() instanceof TemporarySpiderWebBlock) {
			if(Powers.SPIDERWEB.isActive(event.getPlayer())) {
				event.setNewSpeed(1000F);
			}
		}
	}
}
