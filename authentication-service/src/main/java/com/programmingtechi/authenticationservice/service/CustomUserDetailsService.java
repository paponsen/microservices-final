package com.programmingtechi.authenticationservice.service;

import com.programmingtechi.authenticationservice.config.CustomUserDetails;
import com.programmingtechi.authenticationservice.entity.UserCredential;
import com.programmingtechi.authenticationservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialRepository userCredentialRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> credential = userCredentialRepository.findByName(username);
        return  credential.map(CustomUserDetails::new).orElseThrow(()-> new UsernameNotFoundException("User not found with name:"+username));
    }
}
