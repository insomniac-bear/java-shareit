package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {
    List<Booking> findTop2ByItem_IdOrderByStartDesc(Long itemId);

    Optional<Booking> findBookingByItem_IdAndBooker_Id(Long itemId, Long authorId);
}
