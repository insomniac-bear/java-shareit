package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;
import ru.practicum.shareit.util.BookingState;
import ru.practicum.shareit.util.RequestRole;

import java.util.List;

import static ru.practicum.shareit.util.HeaderConst.USER_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto createBooking(@RequestHeader(USER_HEADER) long userId, @Valid @RequestBody NewBookingRequestDto newBooking) {
        log.info("POST /bookings - создание бронирования");
        return service.createBooking(userId, newBooking);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto changeAvailableBooking(@RequestHeader(USER_HEADER) long userId, @PathVariable Long bookingId, @RequestParam(defaultValue = "false") boolean approved) {
        log.info("PATCH /bookings/{}?approved={}", bookingId, approved);
        return service.changeAvailableBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto getBooking(@RequestHeader(USER_HEADER) long userId, @PathVariable Long bookingId) {
        log.info("GET /bookings/{} - получение бронирования от пользователя {}", bookingId, userId);
        return service.getBooking(bookingId, userId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getBookings(@RequestHeader(USER_HEADER) long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("GET /bookings - получение бронирований пользователя {} с параметром state = {}", userId, state);
        return service.getBookings(userId, state, RequestRole.BOOKER);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getBookingsByOwner(@RequestHeader(USER_HEADER) long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("GET /bookings/owner - получение бронирований собственника {} с параметром state = {}", userId, state);
        return service.getBookings(userId, state, RequestRole.OWNER);
    }
}
