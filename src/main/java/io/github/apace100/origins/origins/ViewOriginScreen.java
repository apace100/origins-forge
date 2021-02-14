package io.github.apace100.origins.origins;

import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.apace100.origins.KeyBindings;
import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.capabilities.OriginCapability;
import io.github.apace100.origins.power.Power;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=OriginsMod.MODID, value=Dist.CLIENT)
public class ViewOriginScreen extends Screen {

	private static final ResourceLocation WINDOW = new ResourceLocation(OriginsMod.MODID, "textures/gui/choose_origin.png");
	
	private Origin origin;
	private static final int windowWidth = 176;
	private static final int windowHeight = 182;
	private int scrollPos = 0;
	private int currentMaxScroll = 0;
	private int border = 13;
	
	private int guiTop, guiLeft;
	
	public ViewOriginScreen() {
		super(new TranslationTextComponent(OriginsMod.MODID + ".screen.view_origin"));
		this.origin = Minecraft.getInstance().player.getCapability(OriginCapability.CAPABILITY).map(holder -> holder.getOrigin()).orElse(Origins.HUMAN);
	}

	@Override
	protected void init() {
		super.init();
		guiLeft = (this.width - windowWidth) / 2;
        guiTop = (this.height - windowHeight) / 2;
        addButton(new net.minecraft.client.gui.widget.button.Button(guiLeft + windowWidth / 2 - 50, guiTop + windowHeight + 5, 100, 20, I18n.format(OriginsMod.MODID + ".gui.close"), b -> {
        	Minecraft.getInstance().displayGuiScreen(null);
        }));
        Origins.HUMAN.getDisplayItem().getOrCreateTag().putString("SkullOwner", Minecraft.getInstance().player.getDisplayName().getString());
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderOriginWindow(mouseX, mouseY);
		super.render(mouseX, mouseY, partialTicks);
	}

	private void renderOriginWindow(int mouseX, int mouseY) {
		this.minecraft.getTextureManager().bindTexture(WINDOW);
		GlStateManager.enableBlend();
		renderWindowBackground(16, 0);
		this.renderOriginContent(mouseX, mouseY);
		this.minecraft.getTextureManager().bindTexture(WINDOW);
		this.blit((this.width - windowWidth) / 2, (this.height - windowHeight) / 2, 0, 0, windowWidth, windowHeight);
		renderOriginName();
		ITextComponent title = new StringTextComponent(I18n.format(OriginsMod.MODID + ".gui.view_origin.title"));
		int titleWidth = font.getStringWidth(title.getString());
		this.font.drawStringWithShadow(title.getFormattedText(), width / 2 - titleWidth / 2, (this.height - windowHeight) / 2 - 20, 0xFFFFFF);
		GlStateManager.disableBlend();
	}
	
	private void renderOriginName() {
		ITextComponent name = origin.getDisplayName();
		this.font.drawStringWithShadow(name.getFormattedText(), guiLeft + 39, guiTop + 19, 0xFFFFFF);
		ItemStack is = origin.getDisplayItem();
		this.itemRenderer.renderItemIntoGUI(is, guiLeft + 15, guiTop + 15);
	}
	
	private void renderWindowBackground(int offsetYStart, int offsetYEnd) {
		int left = (this.width - windowWidth) / 2;
		int top = (this.height - windowHeight) / 2;
		//this.blit(left, top, 0, 0, windowWidth, windowHeight);
		int endX = left + windowWidth - border;
		int endY = top + windowHeight - border;
		for(int x = left; x < endX; x += 16) {
			for(int y = top + offsetYStart; y < endY + offsetYEnd; y += 16) {
				this.blit(x, y, windowWidth, 0, Math.max(16, endX - x), Math.max(16, endY + offsetYEnd - y));
			}
		}
		//this.blit(p_blit_1_, p_blit_2_, p_blit_3_, p_blit_4_, p_blit_5_, p_blit_6_);
		//AbstractGui.blit(left + border, top + border, 0, 0, windowWidth - border * 2, windowHeight - border * 2, 16, 16);
	}
	
	@Override
	public boolean mouseScrolled(double x, double y, double z) {
		boolean retValue = super.mouseScrolled(x, y, z);
		int np = this.scrollPos - (int)z * 4;
		if(np < 0) {
			this.scrollPos = 0;
		} else
		if(np > this.currentMaxScroll) {
			this.scrollPos = this.currentMaxScroll;
		} else {
			this.scrollPos = np;
		}
		return retValue;
	}

	private void renderOriginContent(int mouseX, int mouseY) {
		int x = (this.width - windowWidth) / 2 + 18;
		int y = (this.height - windowHeight) / 2 + 40;
		int startY = y;
		int endY = y - 65 + windowHeight;
		y -= scrollPos;
		for(Power p : origin) {
			if(p.isHidden()) {
				continue;
			}
			ITextComponent name = p.getDisplayName().applyTextStyle(TextFormatting.UNDERLINE);
			ITextComponent desc = p.getDescription();
			String drawDesc = this.font.wrapFormattedStringToWidth(desc.getFormattedText(), windowWidth - 36);
			List<String> drawLines = this.font.listFormattedStringToWidth(drawDesc, windowWidth - 36);
			if(y >= startY - 18 && y <= endY + 12) {
				this.font.drawString(name.getFormattedText(), x, y, 0xFFFFFF);
			}
			for(String line : drawLines) {
				y += 12;
				if(y >= startY - 18 && y <= endY + 12) {
					this.font.drawString(line, x + 2, y, 0xCCCCCC);
				}
			}
			
			//this.font.drawString(drawLines, x, y + 12, 0xCCCCCC);
			y += 14;
			
		}
		y += scrollPos;
		currentMaxScroll = y - windowHeight - 15;
		if(currentMaxScroll < 0) {
			currentMaxScroll = 0;
		}
	}

	@SubscribeEvent
	public static void onRenderGameOverlay(RenderGameOverlayEvent event) {
		if(Minecraft.getInstance().currentScreen instanceof ViewOriginScreen) {
			event.setCanceled(event.getType() != ElementType.VIGNETTE);
		}
	}
	
	@SubscribeEvent
	public static void onKeyInput(KeyInputEvent event) {
		if(KeyBindings.VIEW_ORIGIN.isPressed() && Minecraft.getInstance().currentScreen == null) {
			Minecraft.getInstance().displayGuiScreen(new ViewOriginScreen());
		}
	}
}
