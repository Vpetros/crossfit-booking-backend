package com.crossfit.booking.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserResponse {

    private String id;
    private String username;
    private String email;
    private Set<String> roles;
}
