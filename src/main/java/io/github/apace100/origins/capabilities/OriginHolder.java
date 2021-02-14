package io.github.apace100.origins.capabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import io.github.apace100.origins.origins.Origin;
import io.github.apace100.origins.origins.Origins;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.Powers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public class OriginHolder implements IOriginHolder {

	private Origin origin;
	private HashMap<Power, CompoundNBT> powerData;
	
	public OriginHolder() {
		this.origin = Origins.EMPTY;
		this.powerData = new HashMap<>();
	}

	@Override
	public boolean hasOrigin() {
		return origin != null && origin != Origins.EMPTY;
	}

	@Override
	public Origin getOrigin() {
		return origin;
	}

	@Override
	public void setOrigin(Origin origin, PlayerEntity player) {
		if(this.origin != null) {
			this.onRemoved(player);
		}
		this.origin = origin;
		for(Power p : origin) {
			if(!powerData.containsKey(p)) {
				powerData.put(p, p.createPowerData());
			}
		}
		List<Power> toRemove = new ArrayList<Power>();
		for(Power p : powerData.keySet()) {
			if(!origin.hasPower(p)) {
				toRemove.add(p);
			}
		}
		for(Power p : toRemove) {
			powerData.remove(p);
		}
		if(this.origin != null) {
			this.onAdded(player);
		}
	}

	@Override
	public INBT serializeToNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("OriginName", origin.getRegistryName().toString());
		ListNBT dataList = new ListNBT();
		for(Entry<Power, CompoundNBT> entry : powerData.entrySet()) {
			CompoundNBT pdNBT = new CompoundNBT();
			pdNBT.putString("Name", entry.getKey().getRegistryName().toString());
			pdNBT.put("Data", entry.getValue());
			dataList.add(pdNBT);
		}
		nbt.put("PowerData", dataList);
		return nbt;
	}

	@Override
	public void deserializeFromNBT(INBT nbt, PlayerEntity player) {
		CompoundNBT compound = (CompoundNBT)nbt;
		String originName = compound.getString("OriginName");
		try {
			Origin newOrigin = Origins.getByName(originName);
			boolean isNewOriginDifferent = newOrigin != this.origin;
			if(isNewOriginDifferent && player != null) {
				this.onRemoved(player);
			}
			this.origin = newOrigin;
			ListNBT dataList = (ListNBT)compound.get("PowerData");
			this.powerData.clear();
			for(INBT data : dataList) {
				CompoundNBT dataNBT = (CompoundNBT)data;
				String powerName = dataNBT.getString("Name");
				Power power = Powers.getByName(powerName);
				this.powerData.put(power, (CompoundNBT)dataNBT.get("Data"));
			}
			if(isNewOriginDifferent && player != null) {
				this.onAdded(player);
			}
		} catch(IllegalArgumentException e) {
			System.out.println("WARNING: Could not deserialize NBT of OriginHolder for origin with id " + originName + " (no origin with such id.)");
			this.origin = Origins.EMPTY;
			this.powerData.clear();
		}
		
	}
	
	public CompoundNBT getPowerData(Power power) {
		return powerData.get(power);
	}

	public void onAdded(PlayerEntity player) {
		for(Power p : origin) {
			p.onPowerAdded(player);
		}
	}
	
	public void onRemoved(PlayerEntity player) {
		for(Power p : origin) {
			p.onPowerRemoved(player);
		}
	}
	
	public void onTick(PlayerEntity player) {
		for(Power p : origin) {
			if(p.shouldTick()) {
				p.onTick(player);
			}
		}
	}
	
	public void onTickPost(PlayerEntity player) {
		for(Power p : origin) {
			if(p.shouldTick()) {
				p.onTickPost(player);
			}
		}
	}
	
	public void onDeath(PlayerEntity player) {
		for(Power p : origin) {
			powerData.put(p, p.createPowerData());
		}
	}
}
