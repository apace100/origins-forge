package io.github.apace100.origins.network;

import java.util.function.Supplier;

import io.github.apace100.origins.capabilities.OriginCapability;
import io.github.apace100.origins.power.Powers;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncExtraInventoryMessage implements ModMessage {

	private IInventory inv;
	private ListNBT nbt;

	public SyncExtraInventoryMessage() { 
		nbt = new ListNBT();
	}

	public SyncExtraInventoryMessage(IInventory inventory) {
		inv = inventory;
		nbt = new ListNBT();
	}
	

	public static void encode(SyncExtraInventoryMessage msg, PacketBuffer buffer) {
		for(int i = 0; i < 9; i++) {
			ItemStack stack = msg.inv.getStackInSlot(i);
			buffer.writeCompoundTag(stack.write(new CompoundNBT()));
		}
	}

	public static SyncExtraInventoryMessage decode(PacketBuffer buffer) {
		SyncExtraInventoryMessage msg = new SyncExtraInventoryMessage();
		for(int i = 0; i < 9; i++) {
			msg.nbt.add(buffer.readCompoundTag());
		}
		return msg;
	}

	public static void handle(SyncExtraInventoryMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ctx.get().getSender().getCapability(OriginCapability.CAPABILITY).ifPresent(r -> {
				CompoundNBT data = r.getPowerData(Powers.EXTRA_INVENTORY);
				data.put("Inventory", msg.nbt);
			});
			ctx.get().setPacketHandled(true);
		});
	}
}
