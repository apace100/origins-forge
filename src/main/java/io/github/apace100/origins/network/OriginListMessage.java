package io.github.apace100.origins.network;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import io.github.apace100.origins.OriginsMod;
import io.github.apace100.origins.capabilities.OriginCapability;
import io.github.apace100.origins.origins.Origin;
import io.github.apace100.origins.origins.Origins;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class OriginListMessage implements ModMessage {

	private Collection<Origin> origins;
	private CompoundNBT playerOriginNBT;

	public OriginListMessage() { }
	
	public OriginListMessage(Collection<Origin> origins) {
		this.origins = origins.stream().filter(r -> r != Origins.EMPTY && r != Origins.HUMAN).collect(Collectors.toSet());
	}
	
	public OriginListMessage getForPlayer(PlayerEntity player) {
		OriginListMessage newMsg = new OriginListMessage(origins);
		player.getCapability(OriginCapability.CAPABILITY).ifPresent(cap -> {
			newMsg.playerOriginNBT = (CompoundNBT)cap.serializeToNBT();
		});
		return newMsg;
	}

	public static void encode(OriginListMessage msg, PacketBuffer buffer) {
		buffer.writeInt(msg.origins.size());
		msg.origins.forEach(r -> r.write(buffer));
		buffer.writeCompoundTag(msg.playerOriginNBT);
	}

	public static OriginListMessage decode(PacketBuffer buffer) {
		OriginListMessage msg = new OriginListMessage();
		Origin[] origins = new Origin[buffer.readInt()];
		for(int i = 0; i < origins.length; i++) {
			origins[i] = Origin.read(buffer);
		}
		msg.origins = Lists.newArrayList(origins);
		msg.playerOriginNBT = buffer.readCompoundTag();
		return msg;
	}

	public static void handle(OriginListMessage msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Origins.clear();
			
			msg.origins.forEach(Origins::register);
			
			PlayerEntity player = OriginsMod.proxy.getClientPlayer();
			player.getCapability(OriginCapability.CAPABILITY).ifPresent(cap -> {
				cap.deserializeFromNBT(msg.playerOriginNBT, player);
				if(!cap.hasOrigin() || cap.getOrigin() == Origins.EMPTY) {
					OriginsMod.proxy.openOriginGui();
				}
			});
			ctx.get().setPacketHandled(true);
		});
	}
}
