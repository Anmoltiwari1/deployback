package com.example.UC18.Authentication.Service;

import java.util.List;

import com.example.UC18.Authentication.DTO.AddRequest;
import com.example.UC18.Authentication.DTO.CompareRequest;
import com.example.UC18.Authentication.DTO.ConvertRequest;
import com.example.UC18.Authentication.DTO.DivideRequest;
import com.example.UC18.Authentication.DTO.QuantityDTO;
import com.example.UC18.Authentication.DTO.SubtractRequest;
import com.example.UC18.Authentication.entity.QuantityMeasurementEntity;
import com.example.UC18.Authentication.util.IMeasurable;

public interface IQuantityMeasurementService {

	QuantityDTO convert(ConvertRequest request);
	
	boolean compare(CompareRequest request);
	
	QuantityDTO add(AddRequest request);
	
	QuantityDTO subtract(SubtractRequest request);
	
	double divide(DivideRequest request);
	
	List<QuantityMeasurementEntity> getHistory();
}
