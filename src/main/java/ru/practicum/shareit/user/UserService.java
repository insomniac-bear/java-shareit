package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

public interface UserService {
    UserResponseDto getUserById(Long id);

    UserResponseDto createUser(NewUserRequestDto newUserRequestDto);

    UserResponseDto updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto);

    void deleteUser(Long id);
}
