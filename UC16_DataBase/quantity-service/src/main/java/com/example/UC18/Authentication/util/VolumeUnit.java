package com.example.UC18.Authentication.util;

public enum VolumeUnit implements IMeasurable{

	LITER(1.0),
	LITRE(1.0),
	MILLILITER(1.0/1000.0),
	ML(1.0/1000.0),
	GALLON(3.78541);
	
	
	private final double factor;
	
	
	VolumeUnit(double factor) {
		this.factor=factor;
	}


	public double getConversionFactor() {
		return factor;
	}
	
	
	@Override
	public double convertToBaseUnit(double value) {
		return value*factor;
	}
	
	@Override
	public double convertFromBaseUnit(double baseValue) {
		return baseValue/factor;
	}
	
}
