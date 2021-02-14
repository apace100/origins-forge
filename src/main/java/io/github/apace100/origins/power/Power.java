package io.github.apace100.origins.power;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import io.github.apace100.origins.OriginsRegistryEntry;
import io.github.apace100.origins.capabilities.IOriginHolder;
import io.github.apace100.origins.capabilities.OriginCapability;
import io.github.apace100.origins.origins.Origin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;

public class Power extends OriginsRegistryEntry<Power> implements IPower {

	private boolean shouldTick = false;
	private boolean isHidden = false;
	
	private List<Predicate<PlayerEntity>> conditions;
	
	public Power(String name) {
		this.setRegistryName(name);
		conditions = new LinkedList<>();
	}
	
	public Power addCondition(Predicate<PlayerEntity> condition) {
		this.conditions.add(condition);
		return this;
	}
	
	public boolean areConditionsFulfilled(PlayerEntity player) {
		return conditions.size() == 0 || conditions.stream().allMatch(condition -> condition.test(player));
	}
	
	public boolean hasConditions() {
		return this.conditions.size() > 0;
	}
	
	public Power setHidden() {
		this.isHidden = true;
		return this;
	}
	
	public boolean isHidden() {
		return this.isHidden;
	}
	
	public boolean shouldTick() {
		return shouldTick;
	}
	
	public void setTicking() {
		this.shouldTick = true;
	}
	
	public void setEventHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void onTick(PlayerEntity player) { }
	
	public void onTickPost(PlayerEntity player) { }
	
	public void onPowerAdded(PlayerEntity player) { }
	
	public void onPowerRemoved(PlayerEntity player) { }
	
	public CompoundNBT createPowerData() {
		return new CompoundNBT();
	}
	
	public CompoundNBT getPowerData(PlayerEntity player) {
		LazyOptional<IOriginHolder> cap = player.getCapability(OriginCapability.CAPABILITY);
		if(cap.isPresent()) {
			return cap.orElseThrow(IllegalStateException::new).getPowerData(this);
		}
		return null;
	}
	
	public boolean isActive(LivingEntity player) {
		if(player instanceof PlayerEntity) {
			return isActive((PlayerEntity)player);
		}
		return false;
	}
	
	public boolean isActive(PlayerEntity player) {
		LazyOptional<IOriginHolder> cap = player.getCapability(OriginCapability.CAPABILITY);
		if(cap.isPresent()) {
			Origin origin = cap.orElseThrow(IllegalStateException::new).getOrigin();
			return origin.hasPower(this) && areConditionsFulfilled(player);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return getRegistryName().toString();
	}
	
	public String getTranslationKey() {
		return "power." + getRegistryName().getNamespace() + "." + getRegistryName().getPath();
	}
	
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(getTranslationKey() + ".name");
	}
	
	public ITextComponent getDescription() {
		return new TranslationTextComponent(getTranslationKey() + ".desc");
	}
}
