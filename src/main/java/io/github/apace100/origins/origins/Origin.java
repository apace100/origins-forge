package io.github.apace100.origins.origins;

import java.util.Iterator;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.github.apace100.origins.OriginsRegistryEntry;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.Powers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

public class Origin extends OriginsRegistryEntry<Origin> implements Iterable<Power> {
	
	private ImmutableList<Power> powerList;
	private ItemStack displayItem;
	
	private boolean isChoosable = true;
	
	protected static int nextOrder = 0;
	private int order;
	
	public Origin(String registryName, Power... powers) {
		this.setRegistryName(registryName);
		powerList = ImmutableList.copyOf(powers);
		this.order = nextOrder++;
	}
	
	public int getOrder() {
		return this.order;
	}
	
	public Origin setUnchoosable() {
		this.isChoosable = false;
		return this;
	}
	
	public boolean isChoosable() {
		return this.isChoosable;
	}
	
	public Origin setDisplayItem(Item item) {
		this.displayItem = new ItemStack(item);
		return this;
	}
	
	public ItemStack getDisplayItem() {
		return this.displayItem;
	}
	
	public ImmutableList<Power> getPowers() {
		return powerList;
	}
	
	public boolean hasPower(Power power) {
		return powerList.contains(power);
	}

	@Override
	public Iterator<Power> iterator() {
		return powerList.iterator();
	}
	
	@Override
	public String toString() {
		return getRegistryName().toString();
	}
	
	public String getTranslationKey() {
		return "origin." + getRegistryName().getNamespace() + "." + getRegistryName().getPath();
	}
	
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(getTranslationKey() + ".name");
	}
	
	public ITextComponent getDescription() {
		return new TranslationTextComponent(getTranslationKey() + ".desc");
	}
	
	// Only call from client, will throw exception if called on server
	// Add maxLength to buffer.readString to make callable on server
	public static Origin read(PacketBuffer buffer) {
		String regName = buffer.readString();
		int powerCount = buffer.readInt();
		Power[] powers = new Power[powerCount];
		for(int i = 0; i < powerCount; i++) {
			String s = buffer.readString();
			try {
				powers[i] = Powers.getByName(s);
			} catch(IllegalArgumentException e) {
				System.err.println("Failed to get power with id " + s + " which was received from the server in origin " + regName + ".");
				return null;
			}
		}
		Origin origin = new Origin(regName, powers);
		ResourceLocation displayItemId = new ResourceLocation(buffer.readString());
		origin.setDisplayItem(ForgeRegistries.ITEMS.getValue(displayItemId));
		if(buffer.readBoolean()) {
			origin.setUnchoosable();
		}
		origin.order = buffer.readInt();
		return origin;
	}
	
	public void write(PacketBuffer buffer) {
		buffer.writeString(this.getRegistryName().toString());
		buffer.writeInt(this.powerList.size());
		for(Power p : this.powerList) {
			buffer.writeString(p.getRegistryName().toString());
		}
		if(this.getDisplayItem() != null) {
			buffer.writeString(this.getDisplayItem().getItem().getRegistryName().toString());
		} else {
			buffer.writeString(Items.AIR.getRegistryName().toString());
		}
		
		buffer.writeBoolean(!this.isChoosable);
		buffer.writeInt(this.order);
	}
	
	public static Origin read(String name, JsonObject json) {
		JsonArray powerArray = json.getAsJsonArray("powers");
		Power[] powers = new Power[powerArray.size()];
		for(int i = 0; i < powers.length; i++) {
			powers[i] = Powers.getByName(powerArray.get(i).getAsString());
		}
		Item icon = ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("icon").getAsString()));
		if(icon == null) {
			System.out.println("NO ITEM FOUND #### Help me please! ");
			icon = Items.AIR;
		}
		JsonElement unchoosable = json.get("unchoosable");
		boolean isUnchoosable = false;
		if(unchoosable != null) {
			isUnchoosable = unchoosable.getAsBoolean();
		}
		JsonElement order = json.get("order");
		int orderNum = 10000;
		if(order != null) {
			orderNum = order.getAsInt();
		}
		Origin origin = new Origin(name, powers);
		origin.setDisplayItem(icon);
		if(isUnchoosable) {
			origin.setUnchoosable();
		}
		origin.order = orderNum;
		return origin;
	}
	
	public void write(JsonObject json) {
		JsonArray powers = new JsonArray();
		for(Power p : this.powerList) {
			powers.add(p.getRegistryName().toString());
		}
		JsonPrimitive displayItem = new JsonPrimitive(this.getDisplayItem().getItem().getRegistryName().toString());
		JsonPrimitive isUnchoosable = new JsonPrimitive(!this.isChoosable);
		JsonPrimitive order = new JsonPrimitive(this.order);
		json.add("powers", powers);
		json.add("icon", displayItem);
		json.add("unchoosable", isUnchoosable);
		json.add("order", order);
	}
}
