package com.example.UC18.Authentication.util;

public interface IMeasurable {

	public double convertToBaseUnit(double value);
	
	public double convertFromBaseUnit(double Basevalue);

	default void validateOperationSupport(String operation) {
		
	}
}
