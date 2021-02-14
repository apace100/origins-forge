package io.github.apace100.origins.power;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PreventItemUsePower extends Power {

	private Set<Item> unusableItems;
	private List<Tag<Item>> itemTags;
	private Predicate<ItemStack> predicate;

	public PreventItemUsePower(String name, Item... items) {
		super(name);
		unusableItems = Sets.newHashSet(items);
		this.itemTags = new ArrayList<>();
		this.predicate = stack -> isPrevented(stack.getItem());
		this.setEventHandler();
	}
	
	@SafeVarargs
	public PreventItemUsePower(String name, Tag<Item>... tags) {
		super(name);
		unusableItems = new HashSet<>();
		this.itemTags = Lists.newArrayList(tags);
		this.predicate = stack -> isPrevented(stack.getItem());
		this.setEventHandler();
	}
	
	public PreventItemUsePower(String name, Predicate<ItemStack> predicate) {
		super(name);
		this.predicate = predicate;
		this.setEventHandler();
	}
	
	public PreventItemUsePower addItem(Item item) {
		this.unusableItems.add(item);
		return this;
	}
	
	public PreventItemUsePower addTag(Tag<Item> tag) {
		this.itemTags.add(tag);
		return this;
	}

	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Start event) {
		if(event.getEntity() instanceof PlayerEntity) {
			if(this.predicate.test(event.getItem())) {
				if(this.isActive((PlayerEntity)event.getEntity())) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
		if(this.predicate.test(event.getItemStack())) {
			if(this.isActive(event.getPlayer())) {
				event.setResult(Result.DEFAULT);
				event.setCanceled(true);
			}
		}
	}
	
	private boolean isPrevented(Item item) {
		return unusableItems.contains(item) || this.itemTags.stream().anyMatch(item::isIn);
	}

}
