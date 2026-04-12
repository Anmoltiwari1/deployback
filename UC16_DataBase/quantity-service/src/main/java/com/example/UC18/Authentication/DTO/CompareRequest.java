package com.example.UC18.Authentication.DTO;

public class CompareRequest {

	 private QuantityDTO q1;
	    private QuantityDTO q2;

	    public CompareRequest() {}

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
}
