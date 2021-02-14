package io.github.apace100.origins.client;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.fish.WaterBreathingPower;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WaterBreathingPowerClient extends PowerClient {
	
	public WaterBreathingPowerClient(Power wrap) {
		super(wrap);
	}

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
		if(event.getType() == ElementType.AIR) {
			PlayerEntity player = OriginsMod.proxy.getClientPlayer();
			if(this.isActive(player)) {
				CompoundNBT data = (CompoundNBT)this.getPowerData(player);
				int air = data.getInt("Air");
				int w = event.getWindow().getScaledWidth();
				int h = event.getWindow().getScaledHeight();
				event.setCanceled(true);
				renderAir(player, air, WaterBreathingPower.MAX_AIR, w, h);
			}
		}
	}

	private static void renderAir(PlayerEntity player, int value, int max, int width, int height) {
		GlStateManager.enableBlend();
		int left = width / 2 + 91;
		int top = height - ForgeIngameGui.right_height;

		if (value < WaterBreathingPower.MAX_AIR)
		{
			int full = MathHelper.ceil((double)(value - 2) * 10.0D / (double)WaterBreathingPower.MAX_AIR);
			int partial = MathHelper.ceil((double)value * 10.0D / (double)WaterBreathingPower.MAX_AIR) - full;

			for (int i = 0; i < full + partial; ++i)
			{
				AbstractGui.blit(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9, 256, 256);
			}
			ForgeIngameGui.right_height += 10;
		}

		GlStateManager.disableBlend();
	}
}
