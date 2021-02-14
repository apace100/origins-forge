package io.github.apace100.tags;

import io.github.apace100.origins.OriginsMod;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModItemTags {

	public static final Tag<Item> RANGED_WEAPONS = new ItemTags.Wrapper(new ResourceLocation(OriginsMod.MODID, "ranged_weapons"));
	public static final Tag<Item> MEAT = new ItemTags.Wrapper(new ResourceLocation(OriginsMod.MODID, "meat"));
}
