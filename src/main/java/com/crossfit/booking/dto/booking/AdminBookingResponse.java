package com.crossfit.booking.dto.booking;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AdminBookingResponse {

    private String id;

    private String userId;
    private String username;

    private String wodScheduleId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private Instant timestamp;
}
