package com.benjamin.patientservice.repositories;

import com.benjamin.patientservice.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IPatientRepository extends JpaRepository<Patient, UUID> {
	boolean existsByEmailAndIdNot(String email, UUID id);
	boolean existsByEmail(String email);
}
