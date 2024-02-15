package com.programmingtechi.authenticationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthenticationRequest {
    private String userName;
    private String password;
}
