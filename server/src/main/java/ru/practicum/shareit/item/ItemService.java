package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDateResponseDto;
import ru.practicum.shareit.item.dto.NewItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;

import java.util.Collection;

public interface ItemService {
    ItemResponseDto createItem(Long userId, NewItemRequestDto item);

    ItemResponseDto updateItem(Long userId, Long itemId, UpdateItemRequestDto item);

    Collection<ItemWithBookingDateResponseDto> getAllUserItems(Long userId);

    ItemWithBookingDateResponseDto getItemById(Long itemId, Long userId);

    Collection<ItemResponseDto> searchItems(String text);
}
