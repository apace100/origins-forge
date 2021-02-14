package io.github.apace100.origins.entity;

import io.github.apace100.origins.OriginsMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;

public class SuperEnderPearlEntity extends EnderPearlEntity {

	@ObjectHolder(OriginsMod.MODID + ":super_ender_pearl")
	public static EntityType<SuperEnderPearlEntity> TYPE;

	public SuperEnderPearlEntity(EntityType<? extends SuperEnderPearlEntity> type, World worldIn) {
		super(type, worldIn);
	}
	public SuperEnderPearlEntity(World worldIn, double x, double y, double z) {
		super(TYPE, worldIn);
		this.setPosition(x, y, z);
	}

	public SuperEnderPearlEntity(World worldIn, LivingEntity throwerIn) {
		this(worldIn, throwerIn.getPosX(), throwerIn.getPosY() + (double)throwerIn.getEyeHeight() - (double)0.1F, throwerIn.getPosZ());
		this.owner = throwerIn;
	}
	
	private static ItemStack renderItem = new ItemStack(Items.ENDER_PEARL);
	
	@Override
	public ItemStack getItem() {
		return renderItem;
	}
	
	@Override
	protected void onImpact(RayTraceResult result) {
		LivingEntity livingentity = this.owner;
		if (result.getType() == RayTraceResult.Type.ENTITY) {
			Entity entity = ((EntityRayTraceResult)result).getEntity();
			if (entity == this.owner) {
				return;
			}

			entity.attackEntityFrom(DamageSource.causeThrownDamage(this, livingentity), 0.0F);
		}

		if (result.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockRayTraceResult)result).getPos();
			TileEntity tileentity = this.world.getTileEntity(blockpos);
			if (tileentity instanceof EndGatewayTileEntity) {
				EndGatewayTileEntity endgatewaytileentity = (EndGatewayTileEntity)tileentity;
				if (livingentity != null) {
					if (livingentity instanceof ServerPlayerEntity) {
						CriteriaTriggers.ENTER_BLOCK.trigger((ServerPlayerEntity)livingentity, this.world.getBlockState(blockpos));
					}

					endgatewaytileentity.teleportEntity(livingentity);
					this.remove();
					return;
				}

				endgatewaytileentity.teleportEntity(this);
				return;
			}
		}

		for(int i = 0; i < 32; ++i) {
			this.world.addParticle(ParticleTypes.PORTAL, this.getPosX(), this.getPosY() + this.rand.nextDouble() * 2.0D, this.getPosZ(), this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
		}

		if (!this.world.isRemote) {
			if (livingentity instanceof ServerPlayerEntity) {
				ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)livingentity;
				net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(serverplayerentity, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F);
				if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
					if (serverplayerentity.connection.getNetworkManager().isChannelOpen() && serverplayerentity.world == this.world && !serverplayerentity.isSleeping()) {

						if (livingentity.isPassenger()) {
							livingentity.stopRiding();
						}

						livingentity.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
						livingentity.fallDistance = 0.0F;
					}
				}
			} else if (livingentity != null) {
				livingentity.setPositionAndUpdate(this.getPosX(), this.getPosY(), this.getPosZ());
				livingentity.fallDistance = 0.0F;
			}

			this.remove();
		}
	}
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}


}
