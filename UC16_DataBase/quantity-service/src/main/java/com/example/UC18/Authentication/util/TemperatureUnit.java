package com.example.UC18.Authentication.util;

public enum TemperatureUnit implements IMeasurable{

	CELSIUS{
		public double convertToBaseUnit(double value) {
			return value; //base=celsius
		}
		
		public double convertFromBaseUnit(double value) {
			return value;
		}
	},
	
	FAHRENHEIT{
		
		 public double convertToBaseUnit(double value) {
	            return (value - 32) * 5 / 9;
	        }

	        public double convertFromBaseUnit(double value) {
	            return (value * 9 / 5) + 32;
	        }
	},
	
	KELVIN {
        public double convertToBaseUnit(double value) {
            return value - 273.15;
        }

        public double convertFromBaseUnit(double value) {
            return value + 273.15;
        }
	};
	
	@Override
	public void validateOperationSupport(String operation) {
		
		if(operation.equals("DIVIDE") || operation.equals("MULTIPLY")) {		
			throw new UnsupportedOperationException(
					"Temperature does not support"+operation);
		}
	}
}


