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
        ItemResponseDto res = ItemMapper.itemToItemResponseDto(savedItem);

        log.info("Подготовка ответа о созданноой вещи: {}", res);
        return res;
    }

    @Override
    public ItemResponseDto updateItem(Long userId, Long itemId, UpdateItemRequestDto item) {
        userService.getUserById(userId);
        Item updatedItem = itemRepository.update(itemId, item);
        ItemResponseDto res =  ItemMapper.itemToItemResponseDto(updatedItem);

        log.info("Подготовка ответа об обновленной вещи: {}", res);
        return res;
    }

    @Override
    public Collection<ItemResponseDto> getAllUserItems(Long userId) {
        userService.getUserById(userId);
        Collection<ItemResponseDto> res = itemRepository.findAllUsersItems(userId).stream()
                .map(ItemMapper::itemToItemResponseDto)
                .toList();

        log.info("Подготовка ответа о полученных вещах пользователя с id {} - {}", userId, res);
        return res;
    }

    @Override
    public ItemResponseDto getItemById(Long itemId) {
        Item item = itemRepository.getItem(itemId)
                .orElseThrow(() ->  new NotFoundException("Вещь с id " + itemId + " не найдена"));
        ItemResponseDto res = ItemMapper.itemToItemResponseDto(item);

        log.info("Подготовка ответа о найденной вещи с id {} - {}", itemId, res);
        return res;
    }

    @Override
    public Collection<ItemResponseDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        Collection<ItemResponseDto> res = itemRepository.findItemsByText(text).stream()
                .map(ItemMapper::itemToItemResponseDto)
                .toList();

        log.info("Подготовка ответа о найденых вещах по тексту {} - {}", text, res);
        return res;
    }
}
