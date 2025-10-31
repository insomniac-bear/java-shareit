package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.NewItemRequestDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemResponseDto createItem(Long userId, NewItemRequestDto item) {
        userService.getUserById(userId);
        Item savedItem = itemRepository.create(userId, item);
        return ItemMapper.itemToItemResponseDto(savedItem);
    }

    @Override
    public ItemResponseDto updateItem(Long userId, Long itemId, UpdateItemRequestDto item) {
        userService.getUserById(userId);
        Item updatedItem = itemRepository.update(itemId, item);
        return ItemMapper.itemToItemResponseDto(updatedItem);
    }

    @Override
    public Collection<ItemResponseDto> getAllUserItems(Long userId) {
        userService.getUserById(userId);
        return itemRepository.findAllUsersItems(userId).stream()
                .map(ItemMapper::itemToItemResponseDto)
                .toList();
    }

    @Override
    public Collection<ItemResponseDto> getAllItems() {
        return List.of();
    }

    @Override
    public ItemResponseDto getItemById(Long itemId) {
        Item item = itemRepository.getItem(itemId)
                .orElseThrow(() -> {
                    log.error("Вещь с id {} не найдена", itemId);
                    return new NotFoundException("Вещь с id " + itemId + " не найдена");
                });
        return ItemMapper.itemToItemResponseDto(item);
    }

    @Override
    public Collection<ItemResponseDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemRepository.findItemsByText(text).stream()
                .map(ItemMapper::itemToItemResponseDto)
                .toList();
    }
}
