package com.crossfit.booking.service;

import com.crossfit.booking.dto.wod.WodSlotResponse;
import com.crossfit.booking.mapper.WodScheduleMapper;
import com.crossfit.booking.model.WodSchedule;
import com.crossfit.booking.repository.WodScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


@Service
public class WodService {

    private final WodScheduleRepository wodScheduleRepository;
    private final WodScheduleMapper wodScheduleMapper;

    public WodService(WodScheduleRepository wodScheduleRepository, WodScheduleMapper wodScheduleMapper) {
        this.wodScheduleRepository = wodScheduleRepository;
        this.wodScheduleMapper = wodScheduleMapper;
    }

    public List<WodSlotResponse> getCurrentWeekForUser() {

        LocalDate today = LocalDate.now(ZoneId.of("Europe/Athens"));
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate start = monday;
        LocalDate endExclusive = monday.plusWeeks(1);

        return wodScheduleRepository.findWeek(start, endExclusive)
                .stream()
                .map(wodScheduleMapper::toWeekResponse)
                .toList();
    }

    public WodSchedule updateWodDescription(LocalDate date, LocalTime startTime, String description) {
        WodSchedule schedule = wodScheduleRepository
                .findByDateAndStartTime(date, startTime)
                .orElseThrow(() -> new IllegalStateException(
                        "WOD slot not found for date " + date + " and startTime " + startTime
                ));

        schedule.setWorkoutDescription(description);
        return wodScheduleRepository.save(schedule);
    }
}
