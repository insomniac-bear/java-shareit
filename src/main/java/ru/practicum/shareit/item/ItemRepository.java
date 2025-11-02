package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.NewItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    Item create(Long userId, NewItemRequestDto item);

    Item update(Long itemId, UpdateItemRequestDto item);

    Collection<Item> findAllUsersItems(Long userId);

    Collection<Item> findItemsByText(String text);

    Optional<Item> getItem(Long userId);
}
