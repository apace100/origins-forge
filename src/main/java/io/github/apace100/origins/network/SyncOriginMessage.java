package io.github.apace100.origins.network;

import java.util.UUID;
import java.util.function.Supplier;

import io.github.apace100.origins.capabilities.OriginCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncOriginMessage implements ModMessage {

	private String uuid;
	private boolean world;
	private CompoundNBT nbt;

	public SyncOriginMessage() { }

	public SyncOriginMessage(PlayerEntity player) {
		//uuid = player.getCachedUniqueIdString();
		uuid = player.getUniqueID().toString();
		world = false;
		player.getCapability(OriginCapability.CAPABILITY, null).ifPresent((data) -> {
			nbt = (CompoundNBT)data.serializeToNBT();
		});
	}

	public SyncOriginMessage(World world) {
		this.world = true;
		world.getCapability(OriginCapability.CAPABILITY, null).ifPresent((data) -> {
			nbt = (CompoundNBT)data.serializeToNBT();
		});
	}

	public static void encode(SyncOriginMessage msg, PacketBuffer buffer) {
		buffer.writeBoolean(msg.world);
		if(!msg.world) {
			buffer.writeString(msg.uuid);
		}
		buffer.writeCompoundTag(msg.nbt);
	}

	public static SyncOriginMessage decode(PacketBuffer buffer) {
		SyncOriginMessage msg = new SyncOriginMessage();
		msg.world = buffer.readBoolean();
		if(!msg.world) {
			msg.uuid = buffer.readString(32767);
		}
		msg.nbt = buffer.readCompoundTag();
		return msg;
	}

	public static void handle(SyncOriginMessage msg, Supplier<NetworkEvent.Context> ctx) {
		if(Minecraft.getInstance().world != null) {
			PlayerEntity player = Minecraft.getInstance().world.getPlayerByUuid(UUID.fromString(msg.uuid));
			if(player != null) {
				player.getCapability(OriginCapability.CAPABILITY).ifPresent((data) -> {
					data.deserializeFromNBT(msg.nbt, player);
				});
			}
			ctx.get().setPacketHandled(true);
		}
	}
}
