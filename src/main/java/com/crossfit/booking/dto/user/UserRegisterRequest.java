package com.crossfit.booking.dto.user;

import lombok.Data;
import java.util.List;

@Data
public class UserRegisterRequest {

    private String username;
    private String email;
    private String password;
    private List<RoleDto> roles;

    @Data
    public static class RoleDto {
        private String name;
    }
}
