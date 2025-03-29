package com.benjamin.patientservice.services;

import com.benjamin.patientservice.dtos.PatientRequestDto;
import com.benjamin.patientservice.dtos.PatientResponseDto;
import com.benjamin.patientservice.mapper.PatientMapper;
import com.benjamin.patientservice.models.Patient;
import com.benjamin.patientservice.repositories.IPatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService implements IPatientService {

	private final IPatientRepository patientRepository;

	public PatientService(IPatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	@Override
	public List<PatientResponseDto> getPatients() {
		List<Patient> patientList = patientRepository.findAll();
		return patientList
			  .stream()
			  .map(PatientMapper::toDto)
			  .toList();
	}

	@Override
	public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
		Patient patient = patientRepository.save(PatientMapper.toEntity(patientRequestDto));
		return PatientMapper.toDto(patient);
	}
}
