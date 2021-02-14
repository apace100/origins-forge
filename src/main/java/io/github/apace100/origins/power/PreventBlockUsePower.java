package io.github.apace100.origins.power;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.Tag;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PreventBlockUsePower extends Power {

	private Set<Block> unusableBlocks;
	private List<Tag<Block>> blockTags;
	private Predicate<BlockState> predicate;

	public PreventBlockUsePower(String name, Block... blocks) {
		super(name);
		unusableBlocks = Sets.newHashSet(blocks);
		this.blockTags = new ArrayList<>();
		this.predicate = state -> isPrevented(state.getBlock());
		this.setEventHandler();
	}
	
	@SafeVarargs
	public PreventBlockUsePower(String name, Tag<Block>... tags) {
		super(name);
		unusableBlocks = new HashSet<>();
		this.blockTags = Lists.newArrayList(tags);
		this.predicate = state -> isPrevented(state.getBlock());
		this.setEventHandler();
	}
	
	public PreventBlockUsePower(String name, Predicate<BlockState> predicate) {
		super(name);
		this.predicate = predicate;
		this.setEventHandler();
	}
	
	public PreventBlockUsePower addBlock(Block block) {
		this.unusableBlocks.add(block);
		return this;
	}
	
	public PreventBlockUsePower addTag(Tag<Block> tag) {
		this.blockTags.add(tag);
		return this;
	}

	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if(this.predicate.test(event.getWorld().getBlockState(event.getPos()))) {
			event.setUseBlock(Result.DENY);
		}
	}
	
	private boolean isPrevented(Block block) {
		return unusableBlocks.contains(block) || this.blockTags.stream().anyMatch(block::isIn);
	}

}
