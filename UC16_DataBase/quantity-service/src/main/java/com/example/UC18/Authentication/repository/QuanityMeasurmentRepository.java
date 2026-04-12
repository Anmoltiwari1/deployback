package com.example.UC18.Authentication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.UC18.Authentication.entity.QuantityMeasurementEntity;


public interface QuanityMeasurmentRepository extends JpaRepository<QuantityMeasurementEntity,Long>{

	List<QuantityMeasurementEntity> findByOperation(String operation);
	
	List<QuantityMeasurementEntity> findByBooleanResultTrue();
	
	long countByOperation(String operation);
	
}
