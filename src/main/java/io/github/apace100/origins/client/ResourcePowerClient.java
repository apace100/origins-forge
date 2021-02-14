package io.github.apace100.origins.client;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.power.ResourcePower;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ResourcePowerClient extends PowerClient {

	private static ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(OriginsMod.MODID, "textures/gui/resource_bar.png");
	
	private int index = 0;
	
	public ResourcePowerClient(ResourcePower wrap) {
		super(wrap);
		this.index = wrap.getIndex();
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {
		if(event.getType() == ElementType.EXPERIENCE) {
			PlayerEntity player = OriginsMod.proxy.getClientPlayer();
			if(isActive(player)) {
				int posX = event.getWindow().getScaledWidth() / 2 + 20;
				int posY = event.getWindow().getScaledHeight() - 48;
				
				int barWidth = 71;
				int barHeight = 5;
				int iconSize = 8;
				Minecraft mc = Minecraft.getInstance();
				mc.textureManager.bindTexture(OVERLAY_TEXTURE);
				mc.ingameGUI.blit(posX, posY, 0, 0, barWidth, barHeight);
				
				float fill = getFill(player);
				int x = 0;
				int y = 10 + index * 10;
				int w = (int)(fill * barWidth);
				mc.ingameGUI.blit(posX, posY, x, y, w, barHeight);
				mc.ingameGUI.blit(posX - iconSize - 2, posY - 2, 73, 8 + 10 * index, iconSize, iconSize);
			}
		}
	}
	
	private float getFill(PlayerEntity player) {
		return ((ResourcePower)getPower()).getFill(player);
	}
}
