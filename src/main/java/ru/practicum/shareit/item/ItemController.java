package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.NewItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.util.HeaderConst;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto createItem(@RequestHeader(HeaderConst.USER_HEADER) long userId, @Valid @RequestBody NewItemRequestDto item) {
        log.info("POST /items - создание вещи");
        return service.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponseDto updateItem(
            @RequestHeader(HeaderConst.USER_HEADER) long userId, @PathVariable long itemId, @Valid @RequestBody UpdateItemRequestDto item
    ) {
        log.info("PATCH /items - обновление вещи");
        return service.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponseDto getItem(@PathVariable long itemId) {
        log.info("GET /items/{} - получение вещи", itemId);
        return service.getItemById(itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemResponseDto> getUserItems(@RequestHeader(HeaderConst.USER_HEADER) long userId) {
        log.info("GET /items - получение списка вещей пользователя с id {}", userId);
        return service.getAllUserItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemResponseDto> findItems(@RequestParam String text) {
        log.info("POST /items/search={} - поиск вещей содержащих search", text);
        return service.searchItems(text);
    }
}
