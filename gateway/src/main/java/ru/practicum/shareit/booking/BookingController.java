package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;

import java.util.List;

import static ru.practicum.shareit.util.HeaderConst.USER_HEADER;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient client;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(USER_HEADER) long userId, @Valid @RequestBody NewBookingRequestDto newBooking) {
        log.info("POST /bookings - создание бронирования {} пользователем с id {}", newBooking, userId);
        return client.bookItem(userId, newBooking);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> changeAvailableBooking(
            @RequestHeader(USER_HEADER) long userId, @PathVariable Long bookingId,
            @RequestParam(defaultValue = "false") boolean approved) {
        log.info("PATCH /bookings/{}?approved={} - изменение статуса бронирования пользователем с id {}", bookingId, approved, userId);
        return client.changeAvailableBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_HEADER) long userId, @PathVariable Long bookingId) {
        log.info("GET /bookings/{} - получение бронирования от пользователя {}", bookingId, userId);
        return client.getBooking(userId, bookingId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookings(@RequestHeader(USER_HEADER) long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("GET /bookings - получение бронирований пользователя {} с параметром state = {}", userId, state);
        return client.getBookings(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(USER_HEADER) long userId, @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("GET /bookings/owner - получение бронирований собственника {} с параметром state = {}", userId, state);
        return client.getOwnerBookings(userId, state);
    }
}
