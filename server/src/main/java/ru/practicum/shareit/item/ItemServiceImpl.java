package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final CommentService commentService;
    private final ItemMapper mapper;

    @Override
    @Transactional
    public ItemResponseDto createItem(Long userId, NewItemRequestDto newItem) {
        User user = findUser(userId, new NotFoundException("Пользователь не найден"));
        ItemRequest itemRequest = null;
        if (newItem.getRequestId() != null) {
            itemRequest = itemRequestRepository.findById(newItem.getRequestId())
                    .orElseThrow(() -> {
                        log.error("Попытка найти запрос вещи с id {}", newItem.getRequestId());
                        return new NotFoundException("Запрос вещи с id " + newItem.getRequestId() + " не найден");
                    });
        }
        Item savedItem = itemRepository.save(mapper.newItemRequestDtoToItem(newItem, itemRequest, user));
        ItemResponseDto res = mapper.itemToItemResponseDto(savedItem);

        log.info("Подготовка ответа о созданноой вещи: {}", res);
        return res;
    }

    @Override
    @Transactional
    public ItemResponseDto updateItem(Long userId, Long itemId, UpdateItemRequestDto item) {
        User user = findUser(userId, new ForbiddenException("Доступ запрещен"));
        Item savedItem = itemRepository.findById(itemId)
                .orElseThrow(() ->  new NotFoundException("Вещь с id " + itemId + " не найдена"));

        if (!user.equals(savedItem.getUser())) {
            log.info("Передан не корректный id пользователя");
            throw new ValidationException("Вещь с id " + itemId + " у пользователя с id " + userId + " не найдена");
        }

        Item updatingItem = mapper.updateItemField(savedItem, item);
        ItemResponseDto res =  mapper.itemToItemResponseDto(updatingItem);
        log.info("Подготовка ответа об обновленной вещи: {}", res);

        return res;
    }

    @Override
    public Collection<ItemWithBookingDateResponseDto> getAllUserItems(Long userId) {
        findUser(userId, new ForbiddenException("Доступ запрещен"));
        Collection<ItemWithBookingDateResponseDto> res = itemRepository.findAllByUserId(userId).stream()
                .map(item -> {
                    List<Booking> bookingList = bookingRepository.findTop2ByItemIdOrderByStartDesc(item.getId());

                    LocalDateTime nearestBookingDate = null;
                    LocalDateTime lastBookingDate = null;

                    if (!bookingList.isEmpty()) {
                        lastBookingDate = bookingList.get(0).getStart();

                        if (bookingList.size() == 2) {
                            nearestBookingDate = bookingList.get(1).getStart();
                        }
                    }

                    List<CommentResponseDto> comments = commentService.getItemComments(item.getId());

                    return mapper.itemToItemWithBookingDateResponseDto(item, lastBookingDate, nearestBookingDate, comments);
                })
                .toList();

        log.info("Подготовка ответа о полученных вещах пользователя с id {} - {}", userId, res);
        return res;
    }

    @Override
    public ItemWithBookingDateResponseDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->  new NotFoundException("Вещь с id " + itemId + " не найдена"));

        List<Booking> bookingList = bookingRepository.findTop2ByItemIdOrderByStartDesc(item.getId());

        LocalDateTime nearestBookingDate = null;
        LocalDateTime lastBookingDate = null;

        if (!bookingList.isEmpty() && userId.equals(item.getUser().getId())) {
            lastBookingDate = bookingList.get(0).getStart();

            if (bookingList.size() == 2) {
                nearestBookingDate = bookingList.get(1).getStart();
            }
        }

        List<CommentResponseDto> comments = commentService.getItemComments(item.getId());

        ItemWithBookingDateResponseDto res = mapper.itemToItemWithBookingDateResponseDto(item, lastBookingDate, nearestBookingDate, comments);

        log.info("Подготовка ответа о найденной вещи с id {} - {}", itemId, res);
        return res;
    }

    @Override
    public Collection<ItemResponseDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        Collection<ItemResponseDto> res = itemRepository.findAllByNameContainsOrDescriptionContains(text).stream()
                .map(mapper::itemToItemResponseDto)
                .toList();

        log.info("Подготовка ответа о найденых вещах по тексту {} - {}", text, res);
        return res;
    }

    private User findUser(Long userId, RuntimeException exception) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Попытка создать бронировани от несуществующего пользователя с id {}", userId);
                    return exception;
                });
    }
}
