package io.github.apace100.origins.power.fire;

import java.util.Random;

import io.github.apace100.origins.power.UsablePower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ThrowFireballPower extends UsablePower {

	public ThrowFireballPower(String name) {
		super(name, 40);
	}

	@Override
	public void perform(PlayerEntity player) {
		player.world.playEvent((PlayerEntity)null, 1018, new BlockPos(player), 0);
		Random rand = player.getRNG();
		for(int i = 0; i < 1; ++i) {
			float rotationYaw = player.rotationYaw;
			float rotationPitch = player.rotationPitch;
			float pitchOffset = 0f;
			float x = -MathHelper.sin(rotationYaw * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitch * ((float)Math.PI / 180F));
		    float y = -MathHelper.sin((rotationPitch + pitchOffset) * ((float)Math.PI / 180F));
		    float z = MathHelper.cos(rotationYaw * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitch * ((float)Math.PI / 180F));
		    float inaccuracy = 0F;
		    float velocity = 0.1F;
		    Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, rand.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
		    SmallFireballEntity smallfireballentity = new SmallFireballEntity(player.world, player, vec3d.x, vec3d.y, vec3d.z);
		    smallfireballentity.accelerationX = vec3d.x;
		    smallfireballentity.accelerationY = vec3d.y;
		    smallfireballentity.accelerationZ = vec3d.z;
		    float f = MathHelper.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
		    smallfireballentity.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
		    smallfireballentity.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI));
		    smallfireballentity.prevRotationYaw = smallfireballentity.rotationYaw;
		    smallfireballentity.prevRotationPitch = smallfireballentity.rotationPitch;
			
		    double newY = player.getPosY() + (double)(player.getHeight() / 2.0F) + 0.5D;
		    smallfireballentity.setPosition(smallfireballentity.getPosX(), newY, smallfireballentity.getPosZ());
			player.world.addEntity(smallfireballentity);
		}
	}

}
