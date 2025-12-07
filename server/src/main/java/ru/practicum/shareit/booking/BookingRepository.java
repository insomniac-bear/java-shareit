package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {
    List<Booking> findTop2ByItemIdOrderByStartDesc(Long itemId);

    Optional<Booking> findBookingByItemIdAndBookerId(Long itemId, Long authorId);
}
