package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserDtoResponse;

public interface UserService {
    UserDtoResponse getUserById(Long id);

    UserDtoResponse createUser(NewUserRequestDto newUserRequestDto);

    UserDtoResponse updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto);

    void deleteUser(Long id);
}
