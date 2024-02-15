package com.programmingtechi.authenticationservice.repository;

import com.programmingtechi.authenticationservice.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {
    public Optional<UserCredential> findByName(String userName);
}
