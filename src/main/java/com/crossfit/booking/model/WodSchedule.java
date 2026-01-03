package com.crossfit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wod_schedules")
@CompoundIndexes({
        @CompoundIndex(name = "uniq_date_startTime", def = "{'date': 1, 'startTime': 1}", unique = true)
})
public class WodSchedule {

    @Id
    private String id;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String workoutDescription;

    private int capacity;

    private int bookedCount;
}
