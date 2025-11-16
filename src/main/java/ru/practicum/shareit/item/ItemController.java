package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.NewItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDateResponseDto;
import ru.practicum.shareit.item.dto.NewCommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import static ru.practicum.shareit.util.HeaderConst.USER_HEADER;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto createItem(@RequestHeader(USER_HEADER) long userId, @Valid @RequestBody NewItemRequestDto item) {
        log.info("POST /items - создание вещи");
        return itemService.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponseDto updateItem(
            @RequestHeader(USER_HEADER) long userId, @PathVariable long itemId, @Valid @RequestBody UpdateItemRequestDto item
    ) {
        log.info("PATCH /items - обновление вещи");
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemWithBookingDateResponseDto getItem(@RequestHeader(USER_HEADER) long userId, @PathVariable long itemId) {
        log.info("GET /items/{} - получение вещи", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemWithBookingDateResponseDto> getUserItems(@RequestHeader(USER_HEADER) long userId) {
        log.info("GET /items - получение списка вещей пользователя с id {}", userId);
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemResponseDto> findItems(@RequestParam String text) {
        log.info("POST /items/search={} - поиск вещей содержащих search", text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(
            @RequestHeader(USER_HEADER) long userId,
            @PathVariable long itemId,
            @Valid @RequestBody NewCommentRequestDto comment
    ) {
        log.info("POST /items/{}/comments - добавление комментария {} пользователем с id {} ", itemId, comment, userId);
        return commentService.addComment(itemId, userId, comment);
    }
}
