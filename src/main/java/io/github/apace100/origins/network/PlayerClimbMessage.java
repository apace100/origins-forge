package io.github.apace100.origins.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PlayerClimbMessage implements ModMessage {

	public PlayerClimbMessage() { }

	public static void encode(PlayerClimbMessage msg, PacketBuffer buffer) {
		
	}

	public static PlayerClimbMessage decode(PacketBuffer buffer) {
		PlayerClimbMessage msg = new PlayerClimbMessage();
		return msg;
	}

	public static void handle(PlayerClimbMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity sender = ctx.get().getSender();
			sender.fallDistance = 0;
			ctx.get().setPacketHandled(true);
		});
	}
/*
	@Override
	public void fromBytes(ByteBuf buf) {
		world = buf.readBoolean();
		if(!world)
			uuid = ByteBufUtils.readUTF8String(buf);
		nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(world);
		if(!world)
			ByteBufUtils.writeUTF8String(buf, uuid);
		ByteBufUtils.writeTag(buf, nbt);
	}

	public static class Handler implements IMessageHandler<SyncPotionDataMessage, IMessage> {

		@Override
		public IMessage onMessage(SyncPotionDataMessage message, MessageContext ctx) {
			if(Minecraft.getMinecraft().world != null) {
				if(message.world) {
					IPotionData data = Minecraft.getMinecraft().world.getCapability(CapabilityPotionData.POTION_DATA, null);
					if(data != null) {
						data.deserializeFromNBT(message.nbt);
					}
				} else {
					EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByUUID(UUID.fromString(message.uuid));
					if(player != null) {
						IPotionData data = player.getCapability(CapabilityPotionData.POTION_DATA, null);
						if(data != null) {
							data.deserializeFromNBT(message.nbt);
						}
					}
				}
			}
			return null;
		}

	}
 */
}
