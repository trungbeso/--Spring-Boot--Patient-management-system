package com.benjamin.patientservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotNull
	private String name;

	@NotNull
	@Email
	@Column(unique = true)
	private String email;

	@NotNull
	private String address;

	@NotNull
	private LocalDate dateOfBirth;

	@NotNull
	@CreatedDate
	private LocalDate registeredDate;

	@PrePersist
	public void onCreate() {
		this.registeredDate = LocalDate.now();
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setName(@NotNull String name) {
		this.name = name;
	}

	public void setEmail(@NotNull @Email String email) {
		this.email = email;
	}

	public void setAddress(@NotNull String address) {
		this.address = address;
	}

	public void setDateOfBirth(@NotNull LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setRegisteredDate(@NotNull LocalDate registeredDate) {
		this.registeredDate = registeredDate;
	}

	public UUID getId() {
		return id;
	}

	public @NotNull String getName() {
		return name;
	}

	public @NotNull @Email String getEmail() {
		return email;
	}

	public @NotNull String getAddress() {
		return address;
	}

	public @NotNull LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public @NotNull LocalDate getRegisteredDate() {
		return registeredDate;
	}
}
