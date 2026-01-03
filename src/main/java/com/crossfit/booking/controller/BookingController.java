package com.crossfit.booking.controller;


import com.crossfit.booking.auth.CustomUserDetails;
import com.crossfit.booking.dto.booking.BookingResponse;

import com.crossfit.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/{wodScheduleId}")
    public ResponseEntity<BookingResponse> bookWod(
            @PathVariable String wodScheduleId,
            Authentication authentication
    ) {
        String userId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        BookingResponse created = bookingService.book(userId, wodScheduleId);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        String userId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        List<BookingResponse> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable String bookingId, Authentication authentication) {
        String userId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        bookingService.cancelBooking(userId, bookingId);
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}
