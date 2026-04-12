package com.example.UC18.Authentication.util;

public enum WeightUnit implements IMeasurable{
	
	KILOGRAM(1.0),
	KG(1.0),
	GRAM(0.001),
	G(0.001),
	POUNDS(0.453592),
	POUND(0.453592);
	
	private final double factor;
	
	
	WeightUnit(double factor) {
		this.factor=factor;
	}


	public double getConversionFactor() {
		return factor;
	}
	
	//To Kilogram(Base)
	@Override
	public double convertToBaseUnit(double value) {
		return value*factor;
	}
	
	//From Kilogram to target
	@Override
	public double convertFromBaseUnit(double baseValue) {
		return baseValue/factor;
	}
	
}
