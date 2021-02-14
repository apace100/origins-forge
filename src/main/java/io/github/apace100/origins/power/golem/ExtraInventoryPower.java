package io.github.apace100.origins.power.golem;

import java.util.ArrayList;
import java.util.List;

import io.github.apace100.origins.client.ExtraInventoryPowerClient;
import io.github.apace100.origins.network.NetworkHelper;
import io.github.apace100.origins.power.Power;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;

public class ExtraInventoryPower extends Power {

	public ExtraInventoryPower(String name) {
		super(name);
		this.setEventHandler();
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new ExtraInventoryPowerClient(this)));
	}

	public List<ItemStack> getInventory(PlayerEntity player) {
		CompoundNBT data = getPowerData(player);
		ListNBT list = (ListNBT) data.get("Inventory");
		List<ItemStack> inventory = new ArrayList<>();
		for(int i = 0; i < 9; i++) {
			inventory.add(ItemStack.read(list.getCompound(i)));
		}
		return inventory;
	}
	
	@Override
	public CompoundNBT createPowerData() {
		CompoundNBT nbt = super.createPowerData();
		ListNBT inventory = new ListNBT();
		for(int i = 0; i < 9; i++) {
			CompoundNBT itemStackNBT = new CompoundNBT();
			ItemStack.EMPTY.write(itemStackNBT);
			inventory.add(itemStackNBT);
		}
		nbt.put("Inventory", inventory);
		return nbt;
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		World world = event.getEntity().world;
		if(!world.isRemote && isActive(event.getEntityLiving()) && !world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
			PlayerEntity player = (PlayerEntity)event.getEntityLiving();
			dropAllItems(player);
		}
	}
	
	@Override
	public void onPowerRemoved(PlayerEntity player) {
		dropAllItems(player);
	}

	private void dropAllItems(PlayerEntity player) {
		CompoundNBT powerData = getPowerData(player);
		ListNBT invList = (ListNBT) powerData.get("Inventory");
		for(int i = 0; i < 9; i++) {
			ItemStack stack = ItemStack.read((CompoundNBT) invList.get(i));
			if(!stack.isEmpty()) {
				player.dropItem(stack, true, false);
				invList.set(i, ItemStack.EMPTY.write(new CompoundNBT()));
			}
		}
		powerData.put("Inventory", invList);
		NetworkHelper.syncOrigin(player);
	}
}
