package com.programmingtechi.authenticationservice.repository;

import com.programmingtechi.authenticationservice.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {
    public Optional<UserCredential> findByName(String userName);
}
