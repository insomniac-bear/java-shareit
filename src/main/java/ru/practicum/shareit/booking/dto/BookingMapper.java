package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.util.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BookingMapper {
    public Booking newBookingToBooking(NewBookingRequestDto newBooking, Item item, User booker) {
        return Booking.builder()
                .start(newBooking.getStart())
                .end(newBooking.getEnd())
                .status(BookingStatus.WAITING)
                .booker(booker)
                .item(item)
                .build();
    }

    public BookingResponseDto bookingToBookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(UserMapper.userToUserDtoResponse(booking.getBooker()))
                .item(ItemMapper.itemToItemResponseDto(booking.getItem()))
                .build();
    }

    public List<BookingResponseDto> mapToBookingResponseDto(Iterable<Booking> bookings) {
        List<BookingResponseDto> bookingsResponseDto = new ArrayList<>();

        bookings.forEach(booking -> bookingsResponseDto.add(bookingToBookingResponseDto(booking)));

        return bookingsResponseDto;
    }
}
