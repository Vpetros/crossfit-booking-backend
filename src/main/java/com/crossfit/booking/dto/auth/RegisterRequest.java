package com.crossfit.booking.dto.auth;

import lombok.Data;
import java.util.Set;

@Data
public class RegisterRequest {

    private String username;
    private String email;
    private String password;

    // Optional - default ROLE_USER
    private Set<String> roles;
}
