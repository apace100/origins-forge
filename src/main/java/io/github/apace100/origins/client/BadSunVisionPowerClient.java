package io.github.apace100.origins.client;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BadSunVisionPowerClient extends PowerClient {

	private static ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(OriginsMod.MODID, "textures/misc/restricted_vision.png");
	
	public BadSunVisionPowerClient(Power wrap) {
		super(wrap);
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {
		if(event.getType() == ElementType.VIGNETTE) {
			PlayerEntity player = Minecraft.getInstance().player;
			if(isActive(player)) {
				int light = Utils.getSunlight(player.world, player.getPosition());
				float alpha = ((float)light / 15F) * 0.95F;
				renderOverlay(event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight(), alpha);
			}
		}
	}

	private void renderOverlay(int w, int h, float alpha) {
		GlStateManager.disableDepthTest();
		GlStateManager.depthMask(false);
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, alpha);
		GlStateManager.disableAlphaTest();
		Minecraft.getInstance().getTextureManager().bindTexture(OVERLAY_TEXTURE);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(0.0D, (double)h, -90.0D).tex(0.0F, 1.0F).endVertex();
		bufferbuilder.pos((double)w, (double)h, -90.0D).tex(1.0F, 1.0F).endVertex();
		bufferbuilder.pos((double)w, 0.0D, -90.0D).tex(1.0F, 0.0F).endVertex();
		bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0F, 0.0F).endVertex();
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepthTest();
		GlStateManager.enableAlphaTest();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
