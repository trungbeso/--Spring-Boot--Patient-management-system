package com.benjamin.patientservice.services;

import com.benjamin.patientservice.dtos.PatientRequestDto;
import com.benjamin.patientservice.dtos.PatientResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPatientService {

	List<PatientResponseDto> getPatients();

	PatientResponseDto createPatient(PatientRequestDto patientRequestDto);
}
