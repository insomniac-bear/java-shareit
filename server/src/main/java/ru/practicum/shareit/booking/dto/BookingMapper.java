package ru.practicum.shareit.booking.dto;

import org.mapstruct.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {ItemMapper.class}
)
public interface BookingMapper {

    @Mapping(target = "id", ignore = true) // ID генерируется БД
    @Mapping(target = "start", source = "newBooking.start")
    @Mapping(target = "end", source = "newBooking.end")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "status", expression = "java(ru.practicum.shareit.util.BookingStatus.WAITING)")
    Booking newBookingToBooking(NewBookingRequestDto newBooking, Item item, User booker);

    @Mapping(target = "id", source = "booking.id")
    @Mapping(target = "start", source = "booking.start")
    @Mapping(target = "end", source = "booking.end")
    @Mapping(target = "item", source = "booking.item")
    @Mapping(target = "booker", source = "booking.booker")
    @Mapping(target = "status", source = "booking.status")
    BookingResponseDto bookingToBookingResponseDto(Booking booking);

    List<BookingResponseDto> mapToBookingResponseDto(Iterable<Booking> bookings);
}