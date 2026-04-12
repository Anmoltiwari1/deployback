package com.example.UC18.Authentication.util;



public enum LengthUnit implements IMeasurable{

	
	FEET(1.0),
	FOOT(1.0),
	INCHES(1.0/12.0),
	INCH(1.0/12.0),
	YARDS(3.0),
	YARD(3.0),
	CENTIMETERS(0.0328084),
	CENTIMETER(0.0328084);
	
	private final double factor;
	
	
	LengthUnit(double factor) {
		this.factor=factor;
	}
 

	public double getConversionFactor() {
		return factor;
	}
	
	//TO Feet(Base)
	@Override
	public double convertToBaseUnit(double value) {
		return value*factor;
	}
	
	@Override
	public double convertFromBaseUnit(double baseValue) {
		return baseValue/factor;
	}
	
	
}
