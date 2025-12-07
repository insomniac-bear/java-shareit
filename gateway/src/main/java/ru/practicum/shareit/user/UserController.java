package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@Validated
public class UserController {
    private final UserClient client;

    public UserController(UserClient client) {
        this.client = client;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getById(@PathVariable @Positive Long userId) {
        log.info("GET /users/{} - получить пользователя по id", userId);
        return client.getById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Valid @RequestBody NewUserRequestDto newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errors);
        }

        log.info("POST /users - создать пользователя {}", newUser);
        return client.create(newUser);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@PathVariable @Positive Long userId, @RequestBody UpdateUserRequestDto user) {
        log.info("PATCH /users/{} - обновить пользователя {}", userId, user);
        return client.update(userId, user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> delete(@PathVariable @Positive Long userId) {
        log.info("DELETE /users/{} - удалить пользователя", userId);
        return client.delete(userId);
    }
}
