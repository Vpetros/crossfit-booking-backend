package com.crossfit.booking.repository;

import com.crossfit.booking.model.WodSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface WodScheduleRepository extends MongoRepository<WodSchedule, String> {

    List<WodSchedule> findByDateBetweenOrderByDateAscStartTimeAsc(
            LocalDate start,
            LocalDate end
    );

    @Query(value = "{ 'date': { $gte: ?0, $lt: ?1 } }", sort = "{ 'date': 1, 'startTime': 1 }")
    List<WodSchedule> findWeek(
            LocalDate startInclusive,
            LocalDate endExclusive
    );

    Optional<WodSchedule> findByDateAndStartTime(LocalDate date, LocalTime startTime);

    boolean existsByDateAndStartTime(LocalDate date, LocalTime startTime);
}
