package io.github.apace100.tags;

import io.github.apace100.origins.OriginsMod;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModBlockTags {

	public static final Tag<Block> NATURAL_STONE = new BlockTags.Wrapper(new ResourceLocation(OriginsMod.MODID, "natural_stone"));
}
