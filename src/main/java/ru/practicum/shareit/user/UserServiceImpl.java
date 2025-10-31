package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDtoResponse getUserById(Long id) {
        return repository.findById(id)
                .map(UserMapper::userToUserDtoResponse)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", id);
                    return new NotFoundException("Пользователь с id " + id + " не найден");
                });
    }

    @Override
    public UserDtoResponse createUser(NewUserRequestDto newUserRequestDto) {
        Optional<User> existingUser = repository.findByEmail(newUserRequestDto.getEmail());

        if (existingUser.isPresent()) {
            log.error("Попытка создать пользователя {} с существующим email {}", newUserRequestDto, newUserRequestDto.getEmail());
            throw new DuplicatedDataException("Email " + newUserRequestDto.getEmail() + " уже используется");
        }

        User newUser = repository.create(newUserRequestDto);

        return UserMapper.userToUserDtoResponse(newUser);
    }

    @Override
    public UserDtoResponse updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto) {
        Optional<User> updatingUser = repository.findById(userId);

        if (updatingUser.isEmpty()) {
            log.error("Пользователь с id {} не найден", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }


        if (updateUserRequestDto.hasEmail()) {
            Optional<User> existingUser = repository.findByEmail(updateUserRequestDto.getEmail());

            if (existingUser.isPresent() && !Objects.equals(existingUser.get().getId(), userId)) {
                log.error("Попытка создать пользователя {} с существующим email {}", updateUserRequestDto, updateUserRequestDto.getEmail());
                throw new DuplicatedDataException("Email " + updateUserRequestDto.getEmail() + " уже используется");
            }
        }


        User updatedUser = repository.update(userId, updateUserRequestDto);
        return UserMapper.userToUserDtoResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> deletingUser = repository.findById(userId);

        if (deletingUser.isEmpty()) {
            log.error("Пользователь с id {} не найден", userId);
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        boolean isDeleted = repository.delete(deletingUser.get());

        if (!isDeleted) {
            log.error("Внутренняя ошибка при попытку удаления пользователя");
            throw new InternalServerException("Не удалось удалить пользователя. Попробуйте позднее");
        }
    }
}
