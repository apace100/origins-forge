package io.github.apace100.origins.api;

import io.github.apace100.origins.power.Power;

public class RegisterPowerIMC {
	
	private Power power;
	
	public RegisterPowerIMC(Power power) {
		if(power.getRegistryName() == null) {
			throw new IllegalArgumentException("Tried to send a RegisterPowerIMC with a Power which has no registry name.");
		}
		this.power = power;
	}
	
	public Power getPower() {
		return power;
	}
}
