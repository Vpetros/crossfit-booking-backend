package com.crossfit.booking.mapper;

import com.crossfit.booking.dto.wod.WodSlotResponse;
import com.crossfit.booking.model.WodSchedule;
import org.springframework.stereotype.Component;

@Component
public class WodScheduleMapper {

    public WodSlotResponse toWeekResponse(WodSchedule schedule) {

        int available = schedule.getCapacity() - schedule.getBookedCount();

        return WodSlotResponse.builder()
                .id(schedule.getId())
                .date(schedule.getDate())
                .day(schedule.getDate().getDayOfWeek().name())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .workoutDescription(schedule.getWorkoutDescription())
                .capacity(schedule.getCapacity())
                .bookedCount(schedule.getBookedCount())
                .availableSpots(available)
                .full(available <= 0)
                .build();
    }
}
