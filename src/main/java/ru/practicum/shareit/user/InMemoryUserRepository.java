package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long lastId = 0;

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = Optional.ofNullable(users.get(id));
        log.info("Получение пользователя {}", user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> foundUser = users.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
        log.info("Получение пользователя {}", foundUser);
        return foundUser;
    }

    @Override
    public User create(NewUserRequestDto user) {
        User newUser = UserMapper.userDtoToUser(user);
        newUser.setId(lastId);
        users.put(lastId, newUser);
        lastId++;

        log.info("сохранение пользоваетля {}", newUser);
        return newUser;
    }

    @Override
    public User update(Long userId, UpdateUserRequestDto user) {
        User updatingUser = users.get(userId);
        User updatedUser = UserMapper.updateUserField(updatingUser, user);
        users.put(userId, updatedUser);

        log.info("Обновление пользователя с id {} данными {}", userId, user);
        return updatedUser;
    }

    @Override
    public boolean delete(User user) {
        User removedUser = users.remove(user.getId());
        log.info("Удаление пользователя {}", user);
        return removedUser != null;
    }
}
