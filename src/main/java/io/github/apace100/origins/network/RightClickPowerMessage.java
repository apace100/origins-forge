package io.github.apace100.origins.network;

import java.util.function.Supplier;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.Powers;
import io.github.apace100.origins.power.RightClickPower;
import io.github.apace100.origins.power.UsablePower;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class RightClickPowerMessage implements ModMessage {

	private String powerId;

	public RightClickPowerMessage() { }

	public RightClickPowerMessage(Power power) {
		this.powerId = power.getRegistryName().toString();
	}

	public static void encode(RightClickPowerMessage msg, PacketBuffer buffer) {
		buffer.writeString(msg.powerId);
	}

	public static RightClickPowerMessage decode(PacketBuffer buffer) {
		RightClickPowerMessage msg = new RightClickPowerMessage();
		msg.powerId = buffer.readString(32767);
		return msg;
	}

	public static void handle(RightClickPowerMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity sender = ctx.get().getSender();
			Power power = Powers.getByName(msg.powerId);
			if(power instanceof RightClickPower) {
				if(power.isActive(sender)) {
					((RightClickPower)power).performRightClick(sender);
				}
			} else
			if(power instanceof UsablePower) {
				if(power.isActive(sender)) {
					((UsablePower)power).perform(sender);
				}
			}
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
