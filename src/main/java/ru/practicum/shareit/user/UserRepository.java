package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {
    public Optional<User> findById(Long id);

    public Optional<User> findByEmail(String email);

    public User create(NewUserRequestDto user);

    public User update(Long userId, UpdateUserRequestDto user);

    public boolean delete(User user);
}
