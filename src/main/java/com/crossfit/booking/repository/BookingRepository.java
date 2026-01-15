package com.crossfit.booking.repository;

import com.crossfit.booking.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface BookingRepository extends MongoRepository<Booking, String> {

    boolean existsByUserIdAndWodScheduleId(String userId, String wodScheduleId);
    List<Booking> findAllByUserId(String userId);

    // Admin: list all bookings
    List<Booking> findAllByOrderByTimestampDesc();
}
