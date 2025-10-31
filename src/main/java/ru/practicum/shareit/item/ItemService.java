package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.NewItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;

import java.util.Collection;

public interface ItemService {
    ItemResponseDto createItem(Long userId, NewItemRequestDto item);

    ItemResponseDto updateItem(Long userId, Long itemId, UpdateItemRequestDto item);

    Collection<ItemResponseDto> getAllUserItems(Long userId);

    Collection<ItemResponseDto> getAllItems();

    ItemResponseDto getItemById(Long itemId);

    Collection<ItemResponseDto> searchItems(String text);
}
