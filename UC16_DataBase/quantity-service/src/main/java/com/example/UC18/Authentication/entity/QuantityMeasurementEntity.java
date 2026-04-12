package com.example.UC18.Authentication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="measurements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private double value1;
	private String unit1;
	
	private double value2;
	private String unit2;
	
	private String operation;
	
	
	
	@Column(name="numeric_result")
	private double numericResult;
	
	@Column(name="boolean_result")
	private boolean booleanResult;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getValue1() {
		return value1;
	}

	public void setValue1(double value1) {
		this.value1 = value1;
	}

	public String getUnit1() {
		return unit1;
	}

	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}

	public double getValue2() {
		return value2;
	}

	public void setValue2(double value2) {
		this.value2 = value2;
	}

	public String getUnit2() {
		return unit2;
	}

	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public double getNumericResult() {
		return numericResult;
	}

	public void setNumericResult(double numericResult) {
		this.numericResult = numericResult;
	}

	public boolean isBooleanResult() {
		return booleanResult;
	}

	public void setBooleanResult(boolean booleanResult) {
		this.booleanResult = booleanResult;
	}
	
}
