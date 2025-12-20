package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import static ru.practicum.shareit.util.HeaderConst.USER_HEADER;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(USER_HEADER) long userId,
                                         @Valid @RequestBody NewItemRequestDto req) {
        log.info("POST /requests - создание запроса {} от пользователя с id {}", req, userId);
        return client.create(req, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserItemRequests(@RequestHeader(USER_HEADER) long userId) {
        log.info("GET /requests - запрос на список запросов на вещь от пользователя с id {}", userId);
        return client.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(USER_HEADER) long userId) {
        log.info("GET /requests/all - запрос на список всех запросов на вещи от пользователя с id {}", userId);
        return client.getAllItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequest(@RequestHeader(USER_HEADER) long userId, @PathVariable long requestId) {
        log.info("GET /requests/{} - запрос на получение запроса на вещь с id {} от пользователя с id {}", requestId, requestId, userId);
        return client.getItemRequest(requestId, userId);
    }
}
