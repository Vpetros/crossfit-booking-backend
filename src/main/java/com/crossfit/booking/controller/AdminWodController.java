package com.crossfit.booking.controller;

import com.crossfit.booking.dto.wod.UpdateWodRequest;
import com.crossfit.booking.model.WodSchedule;
import com.crossfit.booking.service.WeeklyScheduleGenerator;
import com.crossfit.booking.service.WodService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/wods")
@PreAuthorize("hasRole('ADMIN')")
public class AdminWodController {

    private final WodService wodService;
    private final WeeklyScheduleGenerator weeklyScheduleGenerator;


    public AdminWodController(WodService wodService, WeeklyScheduleGenerator weeklyScheduleGenerator) {
        this.wodService = wodService;
        this.weeklyScheduleGenerator = weeklyScheduleGenerator;

    }

    @PutMapping("/update")
    public ResponseEntity<WodSchedule> updateWod(@RequestBody UpdateWodRequest request) {

        WodSchedule updated = wodService.updateWodDescription(
                request.getDate(),
                request.getStartTime(),
                request.getWorkoutDescription()
        );

        return ResponseEntity.ok(updated);
    }

    @PostMapping("/generate-current-week")
    public ResponseEntity<List<WodSchedule>> generateCurrentWeek() {
        List<WodSchedule> schedules = weeklyScheduleGenerator.generateCurrentWeek();
        return ResponseEntity.ok(schedules);
    }

    @PostMapping("/generate-next-week")
    public ResponseEntity<List<WodSchedule>> generateNextWeek() {
        return ResponseEntity.ok(weeklyScheduleGenerator.generateNextWeek());
    }
}
