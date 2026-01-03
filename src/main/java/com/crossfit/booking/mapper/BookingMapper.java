package com.crossfit.booking.mapper;

import com.crossfit.booking.dto.booking.BookingResponse;
import com.crossfit.booking.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .wodScheduleId(booking.getWodScheduleId())
                .timestamp(booking.getTimestamp())
                .build();
    }
}
