package com.crossfit.booking.service;

import com.crossfit.booking.model.WodSchedule;
import com.crossfit.booking.repository.WodScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

//@Service comment out for future purpose
@RequiredArgsConstructor
public class WodAdminScheduleService {

    private final WodScheduleRepository wodScheduleRepository;
    private final WodScheduleSlotFactory wodScheduleSlotFactory;

    /**
     * Creates ONLY missing slots for the current week (Mon...Sat).
     * Does NOT modify existing documents.
     */
    public List<WodSchedule> generateMissingSlotsForCurrentWeek() {

        LocalDate monday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate sunday = monday.plusDays(6);

        List<WodSchedule> created = new ArrayList<>();

        for (LocalDate date = monday; !date.isAfter(sunday); date = date.plusDays(1)) {

            DayOfWeek dayOfWeek = date.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.SUNDAY) {
                // closed
                continue;
            }

            List<WodScheduleSlotFactory.Slot> slots =
                    (dayOfWeek == DayOfWeek.SATURDAY) ? wodScheduleSlotFactory.saturdaySlots() : wodScheduleSlotFactory.weekdaySlots();

            for (WodScheduleSlotFactory.Slot slot : slots) {

                boolean exists = wodScheduleRepository.existsByDateAndStartTime(date, slot.start());
                if (exists) {
                    continue;
                }

                WodSchedule schedule = WodSchedule.builder()
                        .date(date)
                        .startTime(slot.start())
                        .endTime(slot.end())
                        .workoutDescription("")
                        .capacity(20)
                        .bookedCount(0)
                        .build();

                created.add(wodScheduleRepository.save(schedule));
            }
        }

        return created;
    }
}
