package com.crossfit.booking.controller;

import com.crossfit.booking.dto.wod.WodSlotResponse;
import com.crossfit.booking.service.WodService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wods")
@RequiredArgsConstructor
public class WodController {

    private final WodService wodService;


    @GetMapping("/current-week")
    public ResponseEntity<List<WodSlotResponse>> getCurrentWeekForUser() {
        return ResponseEntity.ok(
                wodService.getCurrentWeekForUser()
        );
    }

}
