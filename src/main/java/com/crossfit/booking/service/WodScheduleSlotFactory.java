package com.crossfit.booking.service;

import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class WodScheduleSlotFactory {

    public record Slot(LocalTime start, LocalTime end) {}

    public List<Slot> weekdaySlots() {
        return List.of(
                new Slot(LocalTime.of(8, 0),  LocalTime.of(9, 0)),
                new Slot(LocalTime.of(9, 0),  LocalTime.of(10, 0)),
                new Slot(LocalTime.of(10, 0), LocalTime.of(11, 0)),
                new Slot(LocalTime.of(17, 0), LocalTime.of(18, 0)),
                new Slot(LocalTime.of(18, 0), LocalTime.of(19, 0)),
                new Slot(LocalTime.of(19, 0), LocalTime.of(20, 0)),
                new Slot(LocalTime.of(20, 0), LocalTime.of(21, 0)),
                new Slot(LocalTime.of(21, 0), LocalTime.of(22, 0))
        );
    }

    public List<Slot> saturdaySlots() {
        return List.of(
                new Slot(LocalTime.of(9, 0),  LocalTime.of(10, 0)),
                new Slot(LocalTime.of(10, 0), LocalTime.of(11, 0)),
                new Slot(LocalTime.of(11, 0), LocalTime.of(12, 0)),
                new Slot(LocalTime.of(12, 0), LocalTime.of(13, 0))
        );
    }
}
