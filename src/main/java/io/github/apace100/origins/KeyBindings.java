package io.github.apace100.origins;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {

	public static KeyBinding VIEW_ORIGIN = new KeyBinding("key." + OriginsMod.MODID + ".view_origin", KeyConflictContext.IN_GAME, InputMappings.getInputByName("key.keyboard.o"), "key.category." + OriginsMod.MODID);
	public static KeyBinding USE_POWER = new KeyBinding("key." + OriginsMod.MODID + ".use_power", KeyConflictContext.IN_GAME, InputMappings.getInputByName("key.mouse.middle"), "key.category." + OriginsMod.MODID);
	
	public static void register() {
		ClientRegistry.registerKeyBinding(VIEW_ORIGIN);
		ClientRegistry.registerKeyBinding(USE_POWER);
	}
}
