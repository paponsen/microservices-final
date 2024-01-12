package com.programmingtechi.authenticationservice.controller;


import com.programmingtechi.authenticationservice.dto.AuthRequest;
import com.programmingtechi.authenticationservice.entity.UserCredential;
import com.programmingtechi.authenticationservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredential user){
        return authService.saveUser(user);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest){
        System.out.println("User name:"+authRequest.getUserName());
        System.out.println("User password:"+authRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(authRequest.getUserName(),authRequest.getPassword()));
        if(authenticate.isAuthenticated()){
                    String token = authService.generateToken(authRequest.getUserName());
                    System.out.println("token: "+token);
                    return token;

        } else {
            System.out.println("Invalid access");
            throw new RuntimeException("Invalid access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token){
        authService.validateToken(token);
        return "Token is valid";
    }
}
