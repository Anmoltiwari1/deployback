package com.example.UC18.Authentication.DTO;

public class AddRequest {

	private QuantityDTO q1;
	private QuantityDTO q2;
    private String unit;
    
    
	public AddRequest(QuantityDTO q1, QuantityDTO q2, String unit) {
		super();
		this.q1 = q1;
		this.q2 = q2;
		this.unit = unit;
	}


	public QuantityDTO getQ1() {
		return q1;
	}


	public void setQ1(QuantityDTO q1) {
		this.q1 = q1;
	}


	public QuantityDTO getQ2() {
		return q2;
	}


	public void setQ2(QuantityDTO q2) {
		this.q2 = q2;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}
    
    
}
