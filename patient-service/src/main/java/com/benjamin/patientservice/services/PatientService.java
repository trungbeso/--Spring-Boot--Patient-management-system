package com.benjamin.patientservice.services;

import com.benjamin.patientservice.dtos.PatientRequestDto;
import com.benjamin.patientservice.dtos.PatientResponseDto;
import com.benjamin.patientservice.exception.EmailAlreadyExistsException;
import com.benjamin.patientservice.exception.PatientNotFoundException;
import com.benjamin.patientservice.grpc.BillingServiceGrpcClient;
import com.benjamin.patientservice.kafka.KafkaProducer;
import com.benjamin.patientservice.mapper.PatientMapper;
import com.benjamin.patientservice.models.Patient;
import com.benjamin.patientservice.repositories.IPatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService implements IPatientService {

	private final IPatientRepository patientRepository;
	private final BillingServiceGrpcClient billingServiceGrpcClient;
	private final KafkaProducer kafkaProducer;

	public PatientService(IPatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer) {
		this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
		this.kafkaProducer = kafkaProducer;
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
		if (patientRepository.existsByEmail(patientRequestDto.getEmail())) {
			throw new EmailAlreadyExistsException("A patient with this email already exists" + patientRequestDto.getEmail());
		}

		Patient newPatient = patientRepository.save(PatientMapper.toEntity(patientRequestDto));
		billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(),newPatient.getEmail());

		kafkaProducer.sendEvent(newPatient);

		return PatientMapper.toDto(newPatient);
	}

	@Override
	public PatientResponseDto updatePatient(UUID id, PatientRequestDto req) {
		Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not " +
			  "found with ID: " + id));
		if (patientRepository.existsByEmailAndIdNot(req.getEmail(), id)) {
			throw new EmailAlreadyExistsException("A patient with this email already exists" + req.getEmail());
		}

		patient.setName(req.getName());
		patient.setAddress(req.getAddress());
		patient.setEmail(req.getEmail());
		patient.setDateOfBirth(LocalDate.parse(req.getDateOfBirth()));
		patient = patientRepository.save(patient);

		return PatientMapper.toDto(patient);
	}

	@Override
	public void deletePatient(UUID id) {
		patientRepository.deleteById(id);
	}
}
