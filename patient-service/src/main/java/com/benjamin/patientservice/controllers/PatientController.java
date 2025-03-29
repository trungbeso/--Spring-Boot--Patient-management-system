package com.benjamin.patientservice.controllers;

import com.benjamin.patientservice.dtos.PatientRequestDto;
import com.benjamin.patientservice.dtos.PatientResponseDto;
import com.benjamin.patientservice.dtos.validators.CreatePatientValidationGroup;
import com.benjamin.patientservice.services.IPatientService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

	IPatientService patientService;

	public PatientController(IPatientService patientService) {
		this.patientService = patientService;
	}

	@GetMapping
	public ResponseEntity<List<PatientResponseDto>> getPatients() {
		return ResponseEntity.ok(patientService.getPatients());
	}

	@PostMapping("/create")
	public ResponseEntity<PatientResponseDto> createPatient(
		  @Validated({Default.class, CreatePatientValidationGroup.class})
		  @RequestBody PatientRequestDto req){
		return ResponseEntity.ok().body(patientService.createPatient(req));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable UUID id,
	                                                        @Validated({Default.class}) @RequestBody PatientRequestDto req
	                                                        ){
		PatientResponseDto response = patientService.updatePatient(id, req);
		return ResponseEntity.ok().body(response);
	}
}
