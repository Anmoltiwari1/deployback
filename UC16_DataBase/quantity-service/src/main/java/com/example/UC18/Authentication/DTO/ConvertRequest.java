package com.example.UC18.Authentication.DTO;

public class ConvertRequest {

	private QuantityDTO quantity;
    private String targetUnit;

    public ConvertRequest() {}

    public QuantityDTO getQuantity() {
        return quantity;
    }

    public void setQuantity(QuantityDTO quantity) {
        this.quantity = quantity;
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }
}
