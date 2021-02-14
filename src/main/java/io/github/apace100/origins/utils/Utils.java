package io.github.apace100.origins.utils;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class Utils {

	public static Direction getFacingFromNeighboringPos(BlockPos from, BlockPos to) {
		return Direction.getFacingFromVector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
	}
	
	public static int getSunlight(World world, BlockPos pos) {
		int i = world.getLightFor(LightType.SKY, pos) - world.getSkylightSubtracted();
        float f = world.getCelestialAngleRadians(1.0F);
        if(i > 0) {
           float f1 = f < (float)Math.PI ? 0.0F : ((float)Math.PI * 2F);
           f = f + (f1 - f) * 0.2F;
           i = Math.round((float)i * MathHelper.cos(f));
        }

        i = MathHelper.clamp(i, 0, 15);
        return i;
	}
}
