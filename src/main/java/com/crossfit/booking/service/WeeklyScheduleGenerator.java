package com.crossfit.booking.service;

import com.crossfit.booking.model.WodSchedule;
import com.crossfit.booking.repository.WodScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Slf4j
@Component
public class WeeklyScheduleGenerator {

    private static final ZoneId ATHENS = ZoneId.of("Europe/Athens");

    private final WodScheduleRepository wodScheduleRepository;

    public WeeklyScheduleGenerator(WodScheduleRepository wodScheduleRepository) {
        this.wodScheduleRepository = wodScheduleRepository;
    }

    /**
     * Automatic job: every Saturday at 15:00 (Europe/Athens via global config)
     */
    @Scheduled(cron = "0 0 15 * * SAT")
    public void generateNextWeekSchedule() {
        List<WodSchedule> result = generateNextWeek();
        log.info("Generated/verified next-week schedules. Total returned: {}", result.size());
    }

    public List<WodSchedule> generateNextWeek() {
        LocalDate nextMonday = LocalDate.now(ATHENS)
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        return generateWeekWindow(nextMonday);
    }

    /**
     * Used for initial seed
     */
    public List<WodSchedule> generateCurrentWeek() {
        LocalDate thisMonday = LocalDate.now(ATHENS)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        return generateWeekWindow(thisMonday);
    }

    /**
     * Creates only missing slots and returns schedules for the given week.
     */
    private List<WodSchedule> generateWeekWindow(LocalDate weekStartMonday) {

        LocalDate startInclusive = weekStartMonday;
        LocalDate endExclusive = weekStartMonday.plusWeeks(1);

        List<WodSchedule> existing = wodScheduleRepository.findWeek(startInclusive, endExclusive);

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

        List<WodSchedule> created = new ArrayList<>();

        for (LocalDate date = startInclusive; date.isBefore(endExclusive); date = date.plusDays(1)) {

            DayOfWeek dow = date.getDayOfWeek();
            if (dow == DayOfWeek.SUNDAY) {
                continue; // closed
            }

            List<LocalTime> starts = (dow == DayOfWeek.SATURDAY) ? saturdayStarts : weekdaysStarts;

            for (LocalTime start : starts) {
                LocalTime end = start.plusHours(1);

                boolean exists = wodScheduleRepository.existsByDateAndStartTime(date, start);
                if (exists) {
                    continue;
                }

                WodSchedule schedule = WodSchedule.builder()
                        .date(date)
                        .startTime(start)
                        .endTime(end)
                        .workoutDescription("")
                        .capacity(20)
                        .bookedCount(0)
                        .build();

                created.add(wodScheduleRepository.save(schedule));
            }
        }

        if (!created.isEmpty()) {
            existing.addAll(created);
        }

        existing.sort(Comparator.comparing(WodSchedule::getDate)
                .thenComparing(WodSchedule::getStartTime));

        return existing;
    }
}

