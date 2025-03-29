package com.benjamin.patientservice.controllers;

import com.benjamin.patientservice.dtos.PatientRequestDto;
import com.benjamin.patientservice.dtos.PatientResponseDto;
import com.benjamin.patientservice.dtos.validators.CreatePatientValidationGroup;
import com.benjamin.patientservice.services.IPatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Patient", description = "API end point for managing Patient")
@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

	IPatientService patientService;

	public PatientController(IPatientService patientService) {
		this.patientService = patientService;
	}

	@GetMapping
	@Operation(summary = "Get all Patient")
	public ResponseEntity<List<PatientResponseDto>> getPatients() {
		return ResponseEntity.ok(patientService.getPatients());
	}

	@PostMapping("/create")
	@Operation(summary = "Create a new Patient")
	public ResponseEntity<PatientResponseDto> createPatient(
		  @Validated({Default.class, CreatePatientValidationGroup.class})
		  @RequestBody PatientRequestDto req){
		return ResponseEntity.ok().body(patientService.createPatient(req));
	}

	@PutMapping("/update/{id}")
	@Operation(summary = "Update a Patient")
	public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable UUID id,
	                                                        @Validated({Default.class}) @RequestBody PatientRequestDto req
	                                                        ){
		PatientResponseDto response = patientService.updatePatient(id, req);
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Delete a Patient")
	public ResponseEntity<Map<String, String>> deletePatient(@PathVariable UUID id){
		patientService.deletePatient(id);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Patient deleted");
		return ResponseEntity.ok(response);
	}
}
