package com.programmingtechi.authenticationservice.Service;

import com.programmingtechi.authenticationservice.entity.UserCredential;
import com.programmingtechi.authenticationservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationService {
    @Autowired
    private UserCredentialRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;
    public String saveUser(UserCredential user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return "User is saved successfully";
    }
    public void validateToken(String token){
        jwtService.validateToken(token);
    }
}
