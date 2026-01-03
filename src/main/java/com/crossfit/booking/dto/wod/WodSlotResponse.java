package com.crossfit.booking.dto.wod;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class WodSlotResponse {

    private String id;

    private LocalDate date;

    private String day;

    private LocalTime startTime;

    private LocalTime endTime;

    private String workoutDescription;

    private int capacity;

    private int bookedCount;

    private int availableSpots;

    private boolean full;
}
