package ru.practicum.shareit.booking;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.util.BookingState;
import ru.practicum.shareit.util.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.RequestRole;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponseDto createBooking(Long userId, NewBookingRequestDto newBooking) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Попытка создать бронировани от несуществующего пользователя с id {}", userId);
                    return new NotFoundException("Пользователь не найден");
                });
        Long bookingItemId = newBooking.getItemId();
        Item bookingItem = itemRepository.findById(bookingItemId)
                .orElseThrow(() -> {
                    log.error("Попытка создать бронировани для несуществующей вещи с id {}", bookingItemId);
                    return new NotFoundException("Доступ запрещен");
                });

        if (!bookingItem.isAvailable()) {
            throw new ValidationException("Вещь с id " + bookingItem.getId() + " не доступна для бронирования");
        }

        Booking booking = bookingRepository.save(BookingMapper.newBookingToBooking(newBooking, bookingItem, booker));
        BookingResponseDto bookingResponse = BookingMapper.bookingToBookingResponseDto(booking);
        log.info("Подготовка ответа бронирования {}", bookingResponse);

        return bookingResponse;
    }

    @Override
    public BookingResponseDto changeAvailableBooking(Long bookingId, boolean isBooking, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Попытка изменить статус бронирования с id {} от несуществующего пользователя с id {}", bookingId, userId);
                    return new ForbiddenException("Доступ запрещен");
                });

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Попытка изменить статус бронирования с id {} от пользователя с id {}", bookingId, userId);
                    return new NotFoundException("Запрос бронирования с id " + bookingId + " не существует");
                });

        if (!booking.getItem().getUser().getId().equals(userId)) {
            log.error("Попытка изменения статуса бронирования вещи {}, которая не принадлежит пользователю {}", booking.getItem(), owner);
            throw new ForbiddenException("Доступ на изменение статуса вещи запрещен");
        }

        BookingStatus status = isBooking ? BookingStatus.APPROVED : BookingStatus.REJECTED;

        booking.setStatus(status);
        BookingResponseDto updatedBooking = BookingMapper.bookingToBookingResponseDto(bookingRepository.save(booking));
        log.info("Подготовка ответа бронирования с обновленным статусом {}", updatedBooking);

        return updatedBooking;
    }

    @Override
    public BookingResponseDto getBooking(Long bookingId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Попытка получть бронирование с id {} от несуществующего пользователя с id {}", bookingId, userId);
                    return new ForbiddenException("Доступ запрещен");
                });
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Попытка получть несуществующее бронирование с id {}", bookingId);
                    return new NotFoundException("Бронирование с id " + bookingId + " не найдено");
                });
        if (!(booking.getBooker().equals(user) || booking.getItem().getUser().equals(user))) {
            log.error("Попытка получть бронирование с id {} от пользователя с id {} не имеющего на это прав", bookingId, userId);
            throw new ForbiddenException("Доступ запрещен");
        }

        BookingResponseDto res = BookingMapper.bookingToBookingResponseDto(booking);
        log.info("Подготовка ответа запрошенного бронирования {}", res);

        return res;
    }

    @Override
    public List<BookingResponseDto> getBookings(Long userId, BookingState state, RequestRole role) {
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Попытка получть бронирования от несуществующего пользователя с id {}", userId);
                    return new ForbiddenException("Доступ запрещен");
                });

        BooleanExpression byUserId = role == RequestRole.BOOKER ?
                QBooking.booking.booker.id.eq(userId) : QBooking.booking.item.user.id.eq(userId);
        BooleanExpression byState = getExpression(state);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Iterable<Booking> bookings = bookingRepository.findAll(byUserId.and(byState), sort);

        List<BookingResponseDto> resBookings = BookingMapper.mapToBookingResponseDto(bookings);
        log.info("Подготовка ответа бронирований пользователя: {}", resBookings);

        return resBookings;
    }

    private BooleanExpression getExpression(BookingState state) {
        return switch (state) {
            case PAST -> QBooking.booking.end.before(LocalDateTime.now());
            case FUTURE -> QBooking.booking.start.after(LocalDateTime.now());
            case CURRENT -> QBooking.booking.start.before(LocalDateTime.now())
                    .and(QBooking.booking.end.after(LocalDateTime.now()));
            case WAITING -> QBooking.booking.status.eq(BookingStatus.WAITING);
            case REJECTED -> QBooking.booking.status.eq(BookingStatus.REJECTED);
            case ALL -> null;
        };
    }
}
