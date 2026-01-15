package com.crossfit.booking.dto.admin;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AdminMembersTabResponse {

    private String username;
    private String email;
    private Instant createdAt;
}
