package com.crossfit.booking.service;

import com.crossfit.booking.model.WodSchedule;
import com.crossfit.booking.repository.WodScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class WeeklyScheduleGenerator {

    private final WodScheduleRepository wodScheduleRepository;

    public WeeklyScheduleGenerator(WodScheduleRepository wodScheduleRepository) {
        this.wodScheduleRepository = wodScheduleRepository;
    }

    /**
     * Automatic job: every Sunday at 23:00 (Europe/Athens via global config)
     */
    @Scheduled(cron = "0 0 23 * * SUN")
    public void generateNextWeekSchedule() {
        List<WodSchedule> result = generateNextWeek();
        log.info("Generated/verified next-week schedules. Total returned: {}", result.size());
    }

    public List<WodSchedule> generateNextWeek() {
        LocalDate nextMonday = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        return generateWeekFrom(nextMonday);
    }

    /**
     * Used for initial seed
     */
    public List<WodSchedule> generateCurrentWeek() {
        LocalDate thisMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return generateWeekFrom(thisMonday);
    }

    /**
     * Creates only missing slots and returns schedules for the given week.
     */
    private List<WodSchedule> generateWeekFrom(LocalDate weekStartMonday) {
        List<WodSchedule> result = new ArrayList<>();

        List<LocalTime> weekdaysStarts = List.of(
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(17, 0),
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                LocalTime.of(20, 0),
                LocalTime.of(21, 0)
        );

        List<LocalTime> saturdayStarts = List.of(
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0)
        );

        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStartMonday.plusDays(i);

            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                continue; // closed
            }

            List<LocalTime> starts = (date.getDayOfWeek() == DayOfWeek.SATURDAY)
                    ? saturdayStarts
                    : weekdaysStarts;

            for (LocalTime start : starts) {
                LocalTime end = start.plusHours(1);

                // Create only if missing
                boolean exists = wodScheduleRepository.existsByDateAndStartTime(date, start);

                if (!exists) {
                    WodSchedule schedule = WodSchedule.builder()
                            .date(date)
                            .startTime(start)
                            .endTime(end)
                            .workoutDescription("")
                            .capacity(20)
                            .bookedCount(0)
                            .build();

                    schedule = wodScheduleRepository.save(schedule);
                    result.add(schedule);
                } else {

                    wodScheduleRepository.findByDateAndStartTime(date, start)
                            .ifPresent(result::add);
                }
            }
        }

        return result;
    }
}

