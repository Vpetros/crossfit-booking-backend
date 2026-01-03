package com.crossfit.booking.repository;

import com.crossfit.booking.model.WodSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface WodScheduleRepository extends MongoRepository<WodSchedule, String> {

    List<WodSchedule> findByDateBetweenOrderByDateAscStartTimeAsc(
            LocalDate start,
            LocalDate end
    );

    Optional<WodSchedule> findByDateAndStartTime(LocalDate date, LocalTime startTime);

    boolean existsByDateAndStartTime(LocalDate date, LocalTime startTime);
}
