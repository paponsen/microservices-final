package com.programmingtechi.authenticationservice.service;

import com.programmingtechi.authenticationservice.entity.UserCredential;
import com.programmingtechi.authenticationservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
    public String saveUser(UserCredential userCredential){
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        userCredentialRepository.save(userCredential);
        return "User is saved successfully";
    }

    public String generateToken(String userName){
        return jwtService.generateToken(userName);
    }
    
    public void validateToken(String token){
        jwtService.validateToken(token);
    }
}
