package io.github.apace100.origins.network;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncHeldItemMessage implements ModMessage {

	private ItemStack stack;

	public SyncHeldItemMessage() { }

	public SyncHeldItemMessage(ItemStack stack) {
		this.stack = stack;
	}
	

	public static void encode(SyncHeldItemMessage msg, PacketBuffer buffer) {
		buffer.writeCompoundTag(msg.stack.write(new CompoundNBT()));
	}

	public static SyncHeldItemMessage decode(PacketBuffer buffer) {
		SyncHeldItemMessage msg = new SyncHeldItemMessage();
		msg.stack = ItemStack.read(buffer.readCompoundTag());
		return msg;
	}

	public static void handle(SyncHeldItemMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ctx.get().getSender().inventory.setItemStack(msg.stack);
			ctx.get().setPacketHandled(true);
		});
	}
}
