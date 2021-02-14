package io.github.apace100.origins.network;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncInventorySlotMessage implements ModMessage {

	private int slot;
	private ItemStack stack;

	public SyncInventorySlotMessage() { }

	public SyncInventorySlotMessage(int slot, ItemStack stack) {
		this.slot = slot;
		this.stack = stack;
	}
	

	public static void encode(SyncInventorySlotMessage msg, PacketBuffer buffer) {
		buffer.writeVarInt(msg.slot);
		buffer.writeCompoundTag(msg.stack.write(new CompoundNBT()));
	}

	public static SyncInventorySlotMessage decode(PacketBuffer buffer) {
		SyncInventorySlotMessage msg = new SyncInventorySlotMessage();
		msg.slot = buffer.readVarInt();
		msg.stack = ItemStack.read(buffer.readCompoundTag());
		return msg;
	}

	public static void handle(SyncInventorySlotMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ctx.get().getSender().inventory.setInventorySlotContents(msg.slot, msg.stack);
			ctx.get().setPacketHandled(true);
		});
	}
}
