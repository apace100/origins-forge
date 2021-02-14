package io.github.apace100.origins.power;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;

public class AttributeModifierPower extends Power {

	private Map<IAttribute, AttributeModifier> attributeModifiers;

	public AttributeModifierPower(String name) {
		super(name);
		this.attributeModifiers = new HashMap<>();
	}

	@Override
	public Power addCondition(Predicate<PlayerEntity> condition) {
		this.setTicking();
		return super.addCondition(condition);
	}

	public AttributeModifierPower addAttributeModifier(IAttribute attribute, AttributeModifier modifier) {
		this.attributeModifiers.put(attribute, modifier);
		return this;
	}
	
	
	
	@Override
	public void onTick(PlayerEntity player) {
		if(this.areConditionsFulfilled(player)) {
			applyAttributeModifiers(player);
		} else {
			removeAttributeModifiers(player);
		}
	}

	@Override
	public void onPowerAdded(PlayerEntity player) {
		if(!hasConditions() || this.areConditionsFulfilled(player)) {
			applyAttributeModifiers(player);
		}
	}

	@Override
	public void onPowerRemoved(PlayerEntity player) {
		removeAttributeModifiers(player);
	}

	public void removeAttributeModifiers(PlayerEntity player) {
		for(Entry<IAttribute, AttributeModifier> entry : this.attributeModifiers.entrySet()) {
			IAttributeInstance iattributeinstance = player.getAttributes().getAttributeInstance(entry.getKey());
			if (iattributeinstance != null) {
				iattributeinstance.removeModifier(entry.getValue());
			}
		}
	}

	public void applyAttributeModifiers(PlayerEntity player) {
		for(Entry<IAttribute, AttributeModifier> entry : this.attributeModifiers.entrySet()) {
			IAttributeInstance iattributeinstance = player.getAttributes().getAttributeInstance(entry.getKey());
			if (iattributeinstance != null) {
				AttributeModifier attributemodifier = entry.getValue();
				iattributeinstance.removeModifier(attributemodifier);
				iattributeinstance.applyModifier(attributemodifier);
			}
		}

	}
}
