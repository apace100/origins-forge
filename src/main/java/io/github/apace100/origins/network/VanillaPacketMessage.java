package io.github.apace100.origins.network;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class VanillaPacketMessage implements ModMessage {

	private IPacket<IClientPlayNetHandler> packet;
	
	public VanillaPacketMessage() { }
	
	public VanillaPacketMessage(IPacket<IClientPlayNetHandler> packet) {
		this.packet = packet;
	}
	
	public static void encode(VanillaPacketMessage msg, PacketBuffer buffer) {
		buffer.writeString(msg.packet.getClass().getName());
		try {
			msg.packet.writePacketData(buffer);
		} catch (IOException e) {
			System.err.println("Could not send VanillaPacketMessage of type " + msg.packet.getClass().getSimpleName());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static VanillaPacketMessage decode(PacketBuffer buffer) {
		VanillaPacketMessage msg = new VanillaPacketMessage();
		String className = buffer.readString(32767);
		try {
			Class<?> cls = Class.forName(className);
			msg.packet = (IPacket<IClientPlayNetHandler>) cls.getConstructor().newInstance();
			msg.packet.readPacketData(buffer);
		} catch (ClassNotFoundException | ClassCastException | IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.err.println("Received wrong VanillaPacketMessage of type " + className + ", could not decode. Stacktrace:");
			e.printStackTrace();
		}
		return msg;
	}
	
	public static void handle(VanillaPacketMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			msg.packet.processPacket(Minecraft.getInstance().getConnection());
		});
		ctx.get().setPacketHandled(true);
	}
}
