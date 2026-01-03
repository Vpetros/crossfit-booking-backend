package com.crossfit.booking.dto.booking;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class BookingResponse {

    private String id;
    private String wodScheduleId;
    private Instant timestamp;
}
