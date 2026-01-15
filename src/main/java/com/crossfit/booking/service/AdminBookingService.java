package com.crossfit.booking.service;

import com.crossfit.booking.dto.booking.AdminBookingResponse;
import com.crossfit.booking.model.Booking;
import com.crossfit.booking.model.User;
import com.crossfit.booking.model.WodSchedule;
import com.crossfit.booking.repository.BookingRepository;
import com.crossfit.booking.repository.UserRepository;
import com.crossfit.booking.repository.WodScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminBookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final WodScheduleRepository wodScheduleRepository;

    public List<AdminBookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAllByOrderByTimestampDesc();

        return bookings.stream().map(b -> {
            User user = userRepository.findById(b.getUserId()).orElse(null);
            WodSchedule schedule = wodScheduleRepository.findById(b.getWodScheduleId()).orElse(null);

            return AdminBookingResponse.builder()
                    .id(b.getId())
                    .userId(b.getUserId())
                    .username(user != null ? user.getUsername() : null)
                    .wodScheduleId(b.getWodScheduleId())
                    .date(schedule != null ? schedule.getDate() : null)
                    .startTime(schedule != null ? schedule.getStartTime() : null)
                    .endTime(schedule != null ? schedule.getEndTime() : null)
                    .timestamp(b.getTimestamp())
                    .build();
        }).toList();
    }
}
