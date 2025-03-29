package com.benjamin.patientservice.controllers;

import com.benjamin.patientservice.dtos.PatientRequestDto;
import com.benjamin.patientservice.dtos.PatientResponseDto;
import com.benjamin.patientservice.services.IPatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientRequestDto req){
		return ResponseEntity.ok().body(patientService.createPatient(req));
	}
}
