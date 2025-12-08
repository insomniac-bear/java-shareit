package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.NewCommentRequestDto;
import ru.practicum.shareit.item.dto.NewItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;

import static ru.practicum.shareit.util.HeaderConst.USER_HEADER;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient client;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(USER_HEADER) Long userId,
                                         @Valid @RequestBody NewItemRequestDto item) {
        log.info("POST /items - создание вещи {}", item);
        return client.create(item, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@RequestHeader(USER_HEADER) Long userId,
                                         @PathVariable Long itemId,
                                         @Valid @RequestBody UpdateItemRequestDto item) {
        log.info("PATCH /items/{} - обновление вещи {}", itemId, item);
        return client.update(itemId, item, userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItem(@RequestHeader(USER_HEADER) long userId, @PathVariable long itemId) {
        log.info("GET /items/{} - получение вещи пользователя с id {}", itemId, userId);
        return client.getById(itemId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserItems(@RequestHeader(USER_HEADER) long userId) {
        log.info("GET /items - получение списка вещей пользователя с id {}", userId);
        return client.getUserItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findItems(@RequestParam String text) {
        log.info("POST /items/search={} - поиск вещей содержащих search", text);
        return client.findItems(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addComment(@RequestHeader(USER_HEADER) long userId,
                                         @PathVariable long itemId, @Valid @RequestBody NewCommentRequestDto comment
    ) {
        log.info("POST /items/{}/comments - добавление комментария {} пользователем с id {} ", itemId, comment, userId);
        return client.addComment(itemId, userId, comment);
    }
}
