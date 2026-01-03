package com.crossfit.booking.service;

import com.crossfit.booking.dto.booking.BookingResponse;
import com.crossfit.booking.exception.*;
import com.crossfit.booking.mapper.BookingMapper;
import com.crossfit.booking.model.Booking;
import com.crossfit.booking.model.WodSchedule;
import com.crossfit.booking.repository.BookingRepository;
import com.crossfit.booking.repository.WodScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;
    private final BookingMapper bookingMapper;
    private final WodScheduleRepository wodScheduleRepository;

    private static final ZoneId ZONE = ZoneId.of("Europe/Athens");

    public BookingResponse book(String userId, String wodScheduleId) {

        // Checks if User already booked
        if (bookingRepository.existsByUserIdAndWodScheduleId(userId, wodScheduleId)) {
            throw new BookingAlreadyExistsException("User already booked this WOD");
        }

        WodSchedule schedule = wodScheduleRepository.findById(wodScheduleId)
                .orElseThrow(() -> new WodScheduleNotFoundException("WOD schedule not found"));

        if (isClosed(schedule.getWorkoutDescription())) {
            throw new WodClosedException("This WOD slot is closed (no workout posted yet)");
        }

        if (isPastSlot(schedule)) {
            throw new PastWodBookingException("Cannot book a past WOD slot");
        }

        // Atomic increment only if bookedCount < capacity
        Query query = new Query(
                Criteria.where("_id").is(wodScheduleId)
                        .andOperator(
                                Criteria.where("$expr").is(
                                        new Document("$lt", List.of("$bookedCount", "$capacity"))
                                )
                        )
        );

        Update update = new Update().inc("bookedCount", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);

        WodSchedule updated = mongoTemplate.findAndModify(
                query,
                update,
                options,
                WodSchedule.class
        );

        if (updated == null) {
            throw new NoAvailableSpotsException("No available spots");
        }

        // Booking creation
        Booking booking = Booking.builder()
                .userId(userId)
                .wodScheduleId(wodScheduleId)
                .timestamp(Instant.now())
                .build();

        Booking saved = bookingRepository.save(booking);

        return bookingMapper.toResponse(saved);
    }

    private boolean isClosed(String workoutDescription) {
        return workoutDescription == null || workoutDescription.trim().isBlank();
    }

    public List<BookingResponse> getUserBookings(String userId) {
        return bookingRepository.findAllByUserId(userId)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    public void cancelBooking(String userId, String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new BookingNotFoundException("Cannot cancel another user's booking");
        }

        // Reduce bookedCount
        boolean decremented = decrementBookedCount(booking.getWodScheduleId());
        if (!decremented) {
            throw new IllegalStateException("Failed to update WOD booked count");
        }

        bookingRepository.delete(booking);
    }

    private boolean decrementBookedCount(String wodScheduleId) {
        Query query = new Query(
                Criteria.where("_id").is(wodScheduleId)
                .and("bookedCount").gt(0));

        Update update = new Update().inc("bookedCount", -1);

        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);

        WodSchedule updated = mongoTemplate.findAndModify(query, update, options, WodSchedule.class);

        return updated != null;
    }

    private boolean isPastSlot(WodSchedule schedule) {
        LocalDateTime slotStart = LocalDateTime.of(schedule.getDate(), schedule.getStartTime());
        LocalDateTime now = LocalDateTime.now(ZONE);


        return now.isAfter(slotStart) || now.isEqual(slotStart);
    }
}
