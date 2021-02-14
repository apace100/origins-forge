package io.github.apace100.origins.client;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.network.ModPacketHandler;
import io.github.apace100.origins.network.SyncExtraInventoryMessage;
import io.github.apace100.origins.network.SyncHeldItemMessage;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.golem.ExtraInventoryPower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.items.ItemHandlerHelper;

public class ExtraInventoryPowerClient extends PowerClient {
	private static ResourceLocation GUI_TEXTURE = new ResourceLocation(OriginsMod.MODID, "textures/gui/extra_inventory.png");
	private static Instance openInstance;

	public ExtraInventoryPowerClient(Power wrap) {
		super(wrap);
	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if(!doesGuiShowExtra(event.getGui())) {
			if(openInstance != null) {
				openInstance.close();

			}
		} else {
			PlayerEntity player = OriginsMod.proxy.getClientPlayer();
			if(isActive(player)) {
				openInstance(player);
			}
		}
	}

	private boolean doesGuiShowExtra(Screen gui) {
		return gui != null && gui instanceof ContainerScreen;
	}

	private void openInstance(PlayerEntity player) {
		List<ItemStack> stacks = ((ExtraInventoryPower)getPower()).getInventory(player);
		Inventory inventory = new Inventory(9);
		for(int i = 0; i < 9; i++) {
			inventory.setInventorySlotContents(i, stacks.get(i));
		}
		openInstance = new Instance(inventory);
		MinecraftForge.EVENT_BUS.register(openInstance);
	}
	
	private void writeInventoryToData(PlayerEntity player, IInventory inventory) {
		CompoundNBT nbt = getPowerData(player);
		ListNBT list = new ListNBT();
		for(int i = 0; i < 9; i++) {
			CompoundNBT itemStackNBT = new CompoundNBT();
			inventory.getStackInSlot(i).write(itemStackNBT);
			list.add(itemStackNBT);
		}
		nbt.put("Inventory", list);
	}

	// TODO: Implement item dragging, implement shift-clicking
	public class Instance {

		private Minecraft mc;
		public IInventory inventory;
		private ItemRenderer itemRenderer;
		private FontRenderer font;
		private MouseHelper mouseHelper;

		private Slot[] slots;

		private int xMin = 0;
		private int xMax = 0;
		private int yMin = 0;
		private int yMax = 0;

		public Instance(IInventory inventory) {
			this.mc = Minecraft.getInstance();
			this.itemRenderer = this.mc.getItemRenderer();	
			this.mouseHelper = this.mc.mouseHelper;
			this.font = mc.fontRenderer;
			this.inventory = inventory;
		}

		private void initSlots() {
			slots = new Slot[9];
			for(int i = 0; i < 9; i++) {
				slots[i] = new Slot(inventory, i, xMin + 5 + 18 * (i % 3), yMin + 8 + 18 * (i / 3));
			}
		}

		@SubscribeEvent
		public void onGuiDraw(GuiContainerEvent.DrawBackground event) {
			if(!doesGuiShowExtra(event.getGuiContainer())) {
				this.close();
				return;
			}
			ContainerScreen<?> invScreen = event.getGuiContainer();
			this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
			int newXMin = invScreen.getGuiLeft() + invScreen.getXSize() - 7;
			int newXMax = newXMin + 65;
			int newYMin = invScreen.getGuiTop() + invScreen.getYSize() - 90;
			int newYMax = newYMin + 68;
			if(event.getGuiContainer() instanceof ChestScreen) {
				newYMin -= 1;
				newYMax -= 1;
			}
			boolean refreshSlots = false;
			if(slots == null || newXMin != xMin) {
				refreshSlots = true;
			}
			xMin = newXMin;
			xMax = newXMax;
			yMin = newYMin;
			yMax = newYMax;
			if(refreshSlots) {
				this.initSlots();
			}
			//int x = event.getGuiContainer().width / 2 + 176 / 2 - 7;
			event.getGuiContainer().blit(xMin, yMin, 0, 0, 65, 68);
			RenderHelper.disableStandardItemLighting();
			Slot hovered = null;
			for(int i = 0; i < 9; i++) {
				drawSlot(slots[i]);
				if(this.isMouseOverSlot(slots[i])) {
					hovered = slots[i];
				}
			}
			
			if(hovered != null && this.mc.player.inventory.getItemStack().isEmpty() && hovered.getHasStack()) {
				renderTooltip(event.getGuiContainer(), hovered.getStack(), this.mouseX(), this.mouseY());
			}

			this.mc.getTextureManager().bindTexture(InventoryScreen.INVENTORY_BACKGROUND);
		}
		
		protected void renderTooltip(Screen screen, ItemStack stack, int mouseX, int mouseY) {
			FontRenderer font = stack.getItem().getFontRenderer(stack);
			GuiUtils.preItemToolTip(stack);
			screen.renderTooltip(screen.getTooltipFromItem(stack), mouseX, mouseY, (font == null ? this.font : font));
			GuiUtils.postItemToolTip();
		}

		private void close() {
			ModPacketHandler.INSTANCE.sendToServer(new SyncExtraInventoryMessage(this.inventory));
			MinecraftForge.EVENT_BUS.unregister(this);
			ExtraInventoryPowerClient.openInstance = null;
			writeInventoryToData(Minecraft.getInstance().player, this.inventory);
		}

		private int mouseX() {
			return (int)(mouseHelper.getMouseX() * (double)this.mc.getMainWindow().getScaledWidth() / (double)this.mc.getMainWindow().getWidth());
		}

		private int mouseY() {
			return (int)(mouseHelper.getMouseY() * (double)this.mc.getMainWindow().getScaledHeight() / (double)this.mc.getMainWindow().getHeight());
		}

		private boolean isMouseOverSlot(Slot slot) {
			int x = mouseX();
			int y = mouseY();
			return x >= slot.xPos && x - slot.xPos < 18 && y >= slot.yPos && y - slot.yPos < 18;
		}

		private void drawSlot(Slot slotIn) {
			int i = slotIn.xPos;
			int j = slotIn.yPos;
			ItemStack itemstack = slotIn.getStack();
			boolean flag = isMouseOverSlot(slotIn);
			boolean flag1 = false; //slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
			String s = null;


			//this.blitOffset = 100;
			this.itemRenderer.zLevel = 100.0F;

			//some stuff about drawing slot backgrounds

			if (!flag1) {
				GlStateManager.enableDepthTest();
				this.itemRenderer.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
				this.itemRenderer.renderItemOverlayIntoGUI(this.font, itemstack, i, j, s);
				GlStateManager.disableDepthTest();
				GlStateManager.enableAlphaTest();
				if (flag) {
					AbstractGui.fill(i, j, i + 16, j + 16, -2130706433);
				}
				
			}

			this.itemRenderer.zLevel = 0.0F;
			//this.blitOffset = 0;
		}

		public void handleMouseClick(int x, int y, boolean right) {
			Slot clickedSlot = null;
			for(int i = 0; i < 9; i++) {
				if(isMouseOverSlot(slots[i])) {
					clickedSlot = slots[i];
				}
			}
			ItemStack heldStack = this.mc.player.inventory.getItemStack();
			if(clickedSlot != null) {
				ItemStack slotStack = clickedSlot.getStack();
				if(heldStack != null && !heldStack.isEmpty()) {
					if(clickedSlot.isItemValid(heldStack)) {
						if(!slotStack.isEmpty()) {
							boolean canStack = ItemHandlerHelper.canItemStacksStack(heldStack, slotStack);
							if(canStack) {
								int maxPut = slotStack.getMaxStackSize() - slotStack.getCount();
								int put = Math.min(maxPut, heldStack.getCount());
								if(right && put > 1) {
									put = 1;
								}
								slotStack.grow(put);
								if(put > 0) {
									if(heldStack.getCount() == put) {
										this.mc.player.inventory.setItemStack(ItemStack.EMPTY);
									} else {
										heldStack.shrink(put);
									}
								} else {
									int maxTake = Math.min(heldStack.getMaxStackSize() - heldStack.getCount(), slotStack.getCount());
									int take = Math.min(maxTake, slotStack.getMaxStackSize());

									if(take > 0) {
										heldStack.grow(take);
										slotStack.shrink(take);
									}
								}
							}
						} else {
							clickedSlot.putStack(right ? heldStack.split(1) : heldStack);
							if(!right)
								this.mc.player.inventory.setItemStack(ItemStack.EMPTY);
						}
					}
				} else {
					int maxTake = Math.min(heldStack.getMaxStackSize() - heldStack.getCount(), slotStack.getCount() / (right ? 2 : 1));
					int take = Math.min(maxTake, slotStack.getMaxStackSize());
					if(take > 0) {
						this.mc.player.inventory.setItemStack(slotStack.split(take));
					}
				}
			}
			ModPacketHandler.INSTANCE.sendToServer(new SyncHeldItemMessage(this.mc.player.inventory.getItemStack()));
			ModPacketHandler.INSTANCE.sendToServer(new SyncExtraInventoryMessage(this.inventory));
		}

		public void handleMouseRelease(int x, int y, boolean right) {
			//System.out.println("click in gui: " + x + ", " + y);
		}

		@SubscribeEvent
		public void onMouseClicked(GuiScreenEvent.MouseClickedEvent.Pre event) {
			if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT || event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				int x = mouseX();
				int y = mouseY();
				if(x >= xMin && x <= xMax) {
					if(y >= yMin && y <= yMax) {
						handleMouseClick(x, y, event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT);
						event.setCanceled(true);
					}
				}
			}
		}

		@SubscribeEvent
		public void onMouseReleased(GuiScreenEvent.MouseReleasedEvent.Pre event) {
			if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT || event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				int x = mouseX();
				int y = mouseY();
				if(x >= xMin && x <= xMax) {
					if(y >= yMin && y <= yMax) {
						handleMouseRelease(x, y, event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT);
						event.setCanceled(true);
					}
				}
			}
		}
	}
}
