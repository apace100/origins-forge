package io.github.apace100.origins.network;

import io.github.apace100.origins.OriginsMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModPacketHandler {

	private static final String PROTOCOL_VERSION = "1";
	
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		    new ResourceLocation(OriginsMod.MODID, "main"),
		    () -> PROTOCOL_VERSION,
		    PROTOCOL_VERSION::equals,
		    PROTOCOL_VERSION::equals
		);
	
	public static void registerMessages() {
		int id = 0;
		INSTANCE.<VanillaPacketMessage>registerMessage(id++, VanillaPacketMessage.class, VanillaPacketMessage::encode, VanillaPacketMessage::decode, VanillaPacketMessage::handle);
		
		INSTANCE.<SyncOriginMessage>registerMessage(id++, SyncOriginMessage.class, SyncOriginMessage::encode, SyncOriginMessage::decode, SyncOriginMessage::handle);
		INSTANCE.<OriginListMessage>registerMessage(id++, OriginListMessage.class, OriginListMessage::encode, OriginListMessage::decode, OriginListMessage::handle);
		INSTANCE.<ChooseOriginMessage>registerMessage(id++, ChooseOriginMessage.class, ChooseOriginMessage::encode, ChooseOriginMessage::decode, ChooseOriginMessage::handle);
		
		INSTANCE.<RightClickPowerMessage>registerMessage(id++, RightClickPowerMessage.class, RightClickPowerMessage::encode, RightClickPowerMessage::decode, RightClickPowerMessage::handle);
		INSTANCE.<PlayerClimbMessage>registerMessage(id++, PlayerClimbMessage.class, PlayerClimbMessage::encode, PlayerClimbMessage::decode, PlayerClimbMessage::handle);
		
		INSTANCE.<SyncExtraInventoryMessage>registerMessage(id++, SyncExtraInventoryMessage.class, SyncExtraInventoryMessage::encode, SyncExtraInventoryMessage::decode, SyncExtraInventoryMessage::handle);
		INSTANCE.<SyncHeldItemMessage>registerMessage(id++, SyncHeldItemMessage.class, SyncHeldItemMessage::encode, SyncHeldItemMessage::decode, SyncHeldItemMessage::handle);
		INSTANCE.<SyncInventorySlotMessage>registerMessage(id++, SyncInventorySlotMessage.class, SyncInventorySlotMessage::encode, SyncInventorySlotMessage::decode, SyncInventorySlotMessage::handle);
	}
}
