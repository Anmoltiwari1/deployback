package com.example.UC18.Authentication.Controller;

import java.io.Console;
import java.net.ResponseCache;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.UC18.Authentication.DTO.AddRequest;
import com.example.UC18.Authentication.DTO.CompareRequest;
import com.example.UC18.Authentication.DTO.ConvertRequest;
import com.example.UC18.Authentication.DTO.DivideRequest;
import com.example.UC18.Authentication.DTO.QuantityDTO;
import com.example.UC18.Authentication.DTO.SubtractRequest;
import com.example.UC18.Authentication.Service.IQuantityMeasurementService;
import com.example.UC18.Authentication.entity.QuantityMeasurementEntity;

@RestController
@RequestMapping("/api")
public class QuantityMeasurementController {

	 private final IQuantityMeasurementService service;

	    public QuantityMeasurementController(IQuantityMeasurementService service) {
	        this.service = service;
	    }

	    @PostMapping("/add")
	    public ResponseEntity<QuantityDTO> performAdd(@RequestBody AddRequest request) {
	       
	            QuantityDTO result = service.add(request);
	            return ResponseEntity.ok(result);
	    }

	    @PostMapping("/subtract")
	    public  ResponseEntity<QuantityDTO> performSubtract(@RequestBody SubtractRequest request) {
	    	
	    	QuantityDTO result = service.subtract(request);
	        return ResponseEntity.ok(result);
	        
	    }

	    @PostMapping("/divide")
	    public ResponseEntity<Double> performDivide(@RequestBody DivideRequest request) {
	            double result = service.divide(request);
	            return ResponseEntity.ok(result);
	    }
	    

	    @PostMapping("/convert")
	    public ResponseEntity<QuantityDTO> performConvert(@RequestBody ConvertRequest request) {
	            QuantityDTO result = service.convert(request);
	            return ResponseEntity.ok(result);
	          
	        }

	    @PostMapping("/compare")
	    public ResponseEntity<Boolean> performCompare(@RequestBody CompareRequest request) {
	    
	            boolean result = service.compare(request);
	            return ResponseEntity.ok(result);
	    }
	    
	    @GetMapping("/history")
	    public ResponseEntity<List<QuantityMeasurementEntity>> getHistory() {
	        return ResponseEntity.ok(service.getHistory());
	    }
}
