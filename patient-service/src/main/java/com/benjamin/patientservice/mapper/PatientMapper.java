package com.benjamin.patientservice.mapper;

import com.benjamin.patientservice.dtos.PatientRequestDto;
import com.benjamin.patientservice.dtos.PatientResponseDto;
import com.benjamin.patientservice.models.Patient;

import java.time.LocalDate;

public class PatientMapper {
	public static PatientResponseDto toDto(Patient patient) {
		PatientResponseDto patientDto = new PatientResponseDto();
		patientDto.setId(patient.getId().toString());
		patientDto.setName(patient.getName());
		patientDto.setEmail(patient.getEmail());
		patientDto.setAddress(patient.getAddress());
		patientDto.setDateOfBirth(patient.getDateOfBirth().toString());
		return patientDto;
	}

	public static Patient toEntity(PatientRequestDto patientDto) {
		Patient patient = new Patient();
		patient.setName(patientDto.getName());
		patient.setEmail(patientDto.getEmail());
		patient.setAddress(patientDto.getAddress());
		patient.setDateOfBirth(LocalDate.parse(patientDto.getDateOfBirth()));
		patient.setRegisteredDate(LocalDate.parse(patientDto.getRegisteredDate()));
		return patient;
	}
}
