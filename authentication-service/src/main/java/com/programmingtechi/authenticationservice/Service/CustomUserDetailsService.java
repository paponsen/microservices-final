package com.programmingtechi.authenticationservice.Service;

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
    UserCredentialRepository userCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserCredential> userCredential=userCredentialRepository.findByName(userName);
        return userCredential.map(credential-> new CustomUserDetails(credential))
                .orElseThrow(()-> new UsernameNotFoundException("User not found with name "+userName));
    }
}
