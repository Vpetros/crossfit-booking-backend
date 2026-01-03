package com.crossfit.booking.dto.wod;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UpdateWodRequest {

    private LocalDate date;
    private LocalTime startTime;
    private String workoutDescription;
}
