package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.NewItemRequestDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long lastId = 0;

    @Override
    public Item create(Long userId, NewItemRequestDto item) {
        Item newItem = ItemMapper.newItemRequestDtoToItem(item);
        newItem.setId(lastId);
        newItem.setUserId(userId);
        items.put(lastId, newItem);
        lastId++;
        log.info("Создание вещи {}", newItem);
        return newItem;
    }

    @Override
    public Item update(Long itemId, UpdateItemRequestDto item) {
        Item updatingItem = items.get(itemId);

        log.info("Обновление вещи с id {} данными {}", itemId, item);
        return ItemMapper.updateItemField(updatingItem, item);
    }

    @Override
    public Collection<Item> findAllUsersItems(Long userId) {
        Collection<Item> userItems = items.values().stream()
                .filter(item -> Objects.equals(item.getUserId(), userId))
                .toList();

        log.info("Получение списка вещей {} пользователя с id {}", userItems, userId);
        return userItems;
    }

    @Override
    public Collection<Item> findItemsByText(String text) {
        String lowerCaseText = text.toLowerCase();
        Collection<Item> foundedItems = items.values().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerCaseText) ||
                        item.getDescription().toLowerCase().contains(lowerCaseText))
                .toList();

        log.info("Поиск вещей {} содержащих текст {}", foundedItems, text);
        return foundedItems;
    }

    @Override
    public Optional<Item> getItem(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }
}
