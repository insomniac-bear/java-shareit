package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long lastId = 0;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    @Override
    public User create(NewUserRequestDto user) {
        User newUser = UserMapper.userDtoToUser(user);
        newUser.setId(lastId);
        users.put(lastId, newUser);
        lastId++;

        return newUser;
    }

    @Override
    public User update(Long userId, UpdateUserRequestDto user) {
        User updatingUser = users.get(userId);
        User updatedUser = UserMapper.updateUserField(updatingUser, user);
        users.put(userId, updatedUser);
        return updatedUser;
    }

    @Override
    public boolean delete(User user) {
        User removedUser = users.remove(user.getId());
        return removedUser != null;
    }
}
