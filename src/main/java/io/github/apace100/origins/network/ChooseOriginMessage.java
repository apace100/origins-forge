package io.github.apace100.origins.network;

import java.util.function.Supplier;

import io.github.apace100.origins.capabilities.OriginCapability;
import io.github.apace100.origins.origins.Origin;
import io.github.apace100.origins.origins.Origins;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ChooseOriginMessage implements ModMessage {

	private String originRegistryName;

	public ChooseOriginMessage() { }

	public ChooseOriginMessage(Origin origin) {
		this(origin.getRegistryName().toString());
	}
	
	public ChooseOriginMessage(String name) {
		this.originRegistryName = name;
	}

	public static void encode(ChooseOriginMessage msg, PacketBuffer buffer) {
		buffer.writeString(msg.originRegistryName);
	}

	public static ChooseOriginMessage decode(PacketBuffer buffer) {
		ChooseOriginMessage msg = new ChooseOriginMessage();
		msg.originRegistryName = buffer.readString(32767);
		return msg;
	}

	public static void handle(ChooseOriginMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			setOrigin(ctx.get().getSender(), Origins.getByName(msg.originRegistryName));
			ctx.get().setPacketHandled(true);
		});
	}
	
	private static void setOrigin(PlayerEntity player, Origin origin) {
		player.getCapability(OriginCapability.CAPABILITY).ifPresent((r) -> {
			r.setOrigin(origin, player);
			NetworkHelper.sendToAllTracking(new SyncOriginMessage(player), player);
		});
		
	}
}
