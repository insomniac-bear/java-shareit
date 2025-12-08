package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;
import ru.practicum.shareit.util.BookingState;
import ru.practicum.shareit.util.RequestRole;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(Long userId, NewBookingRequestDto newBooking);

    BookingResponseDto changeAvailableBooking(Long bookingId, boolean isBooking, Long userId);

    BookingResponseDto getBooking(Long bookingId, Long userId);

    List<BookingResponseDto> getBookings(Long userId, BookingState state, RequestRole role);
}
