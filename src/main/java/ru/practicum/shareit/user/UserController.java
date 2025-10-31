package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserDtoResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDtoResponse getUser(@PathVariable Long userId) {
        log.info("GET /users/{} - получить пользователя по id", userId);
        return service.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoResponse createUser(@Valid @RequestBody NewUserRequestDto newUserRequestDto) {
        log.info("POST /users - создать пользователя {}", newUserRequestDto);
        return service.createUser(newUserRequestDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDtoResponse updateUser(@PathVariable Long userId, @Valid @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        log.info("PATCH /users/{} - обновить пользователя {}", userId, updateUserRequestDto);
        return service.updateUser(userId, updateUserRequestDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long userId) {
        log.info("DELETE /users/{} - удалить пользователя", userId);
        service.deleteUser(userId);
    }
}
