package com.benjamin.authservice.repositories;

import com.benjamin.authservice.models.Uzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Uzer, UUID> {
    Uzer findByEmail(String email);

    Uzer findByEmailAndPhoneNumber(String email, String phoneNumber);
}
