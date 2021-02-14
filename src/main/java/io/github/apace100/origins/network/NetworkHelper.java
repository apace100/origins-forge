package io.github.apace100.origins.network;

import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class NetworkHelper {

//~~~~ CONVENIENCE METHODS ~~~~//
	public static void syncOrigin(PlayerEntity player) {
		sendToAllTracking(new SyncOriginMessage(player), player);
	}
	
//~~~~ MESSAGES ~~~~//
	public static void sendToAll(ModMessage message) {
		ModPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), message);
	}
	
	public static void sendTo(ModMessage message, PlayerEntity player) {
		sendTo(message, (ServerPlayerEntity)player);
	}
	
	public static void sendTo(ModMessage message, ServerPlayerEntity player) {
		ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
	}
	
	public static void sendToAllTracking(ModMessage message, Entity trackedEntity) {
		ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> trackedEntity), message);
	}
	
	public static void sendToAllTrackingNoSelf(ModMessage message, Entity trackedEntity) {
		ModPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> trackedEntity), message);
	}
	
	public static void sendToAllTracking(ModMessage message, PacketDistributor.TargetPoint targetPoint) {
		ModPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> targetPoint), message);
	}
	
	public static void sendToAllTracking(ModMessage message, World world, BlockPos pos) {
		PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 16, world.getDimension().getType());
		sendToAllTracking(message, targetPoint);
	}
	
//~~~~ VANILLA PACKETS ~~~~//
	public static void sendToAll(IPacket<IClientPlayNetHandler> packet) {
		sendToAll(new VanillaPacketMessage(packet));
	}
	
	public static void sendTo(IPacket<IClientPlayNetHandler> packet, PlayerEntity player) {
		sendTo(new VanillaPacketMessage(packet), player);
	}
	
	public static void sendTo(IPacket<IClientPlayNetHandler> packet, ServerPlayerEntity player) {
		sendTo(new VanillaPacketMessage(packet), player);
	}
	
	public static void sendToAllTracking(IPacket<IClientPlayNetHandler> packet, Entity trackedEntity) {
		sendToAllTracking(new VanillaPacketMessage(packet), trackedEntity);
	}
	
	public static void sendToAllTracking(IPacket<IClientPlayNetHandler> packet, PacketDistributor.TargetPoint targetPoint) {
		sendToAllTracking(new VanillaPacketMessage(packet), targetPoint);
	}
	
	public static void sendToAllTracking(IPacket<IClientPlayNetHandler> packet, World world, BlockPos pos) {
		sendToAllTracking(new VanillaPacketMessage(packet), world, pos);
	}
}
