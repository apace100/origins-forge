package io.github.apace100.origins.client;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.phantom.ColorblindPower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ColorblindPowerClient extends PowerClient {

	private static ResourceLocation SHADER_LOCATION = new ResourceLocation(OriginsMod.MODID, "shaders/grayscale.json");
	
	private static boolean renderedHandLast = true;
	private static boolean renderedHandCheck = false;
	
	public ColorblindPowerClient(Power wrap) {
		super(wrap);
	}
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		if(ColorblindPower.refreshShader && event.phase == Phase.START) {
			refreshShader();
		} else
		if(event.phase == Phase.START) {
			renderedHandCheck = true;
		} else
		if(event.phase == Phase.END) {
			if(renderedHandCheck && renderedHandLast) {
				renderedHandLast = false;
				refreshShader();
			} else
			if(!renderedHandCheck && !renderedHandLast){
				renderedHandLast = true;
				refreshShader();
			}
		}
	}
	
	private void refreshShader() {
		ColorblindPower.refreshShader = false;
		Minecraft mc = Minecraft.getInstance();
		GameRenderer render = mc.gameRenderer;
		PlayerEntity clientPlayer = OriginsMod.proxy.getClientPlayer();
		if(clientPlayer != null && this.isActive(clientPlayer)) {
			render.loadShader(SHADER_LOCATION);
		} else {
			render.loadEntityShader(null);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onRenderHand(RenderHandEvent event) {
		renderedHandCheck = false;
	}
}
