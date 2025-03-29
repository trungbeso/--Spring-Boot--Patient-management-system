package com.benjamin.patientservice.services;

import com.benjamin.patientservice.dtos.PatientRequestDto;
import com.benjamin.patientservice.dtos.PatientResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface IPatientService {

	List<PatientResponseDto> getPatients();

	PatientResponseDto createPatient(PatientRequestDto patientRequestDto);

	PatientResponseDto updatePatient(UUID id, PatientRequestDto req);
}
