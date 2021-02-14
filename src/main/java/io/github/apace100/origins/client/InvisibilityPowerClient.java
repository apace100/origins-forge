package io.github.apace100.origins.client;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.Powers;
import io.github.apace100.origins.utils.Reflection;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class InvisibilityPowerClient extends PowerClient {

	public InvisibilityPowerClient(Power wrap) {
		super(wrap);
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre event) {
		PlayerEntity player = event.getPlayer();
		boolean invis = false;
		if(this.isActive(player)) {
			CompoundNBT data = this.getPowerData(player);
			if(data.getBoolean("Active")) {
				//event.getRenderer().getRenderManager().setRenderShadow(false);
				event.setCanceled(true);
				invis = true;
			}
		}

		ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, event.getRenderer(), invis ? 0.0f : 0.5f, Reflection.SHADOW_SIZE);

	}
	
	@SubscribeEvent
	public void onRenderHand(RenderHandEvent event) {
		if(Powers.INVISIBILITY.isInvisible(OriginsMod.proxy.getClientPlayer())) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Post event) {
		//event.getRenderer().getRenderManager().setRenderShadow(true);
		//ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, event.getRenderer(), invis ? 0.0f : 0.5f, Reflection.SHADOW_SIZE);

	}
}
