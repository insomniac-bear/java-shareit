package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
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
    public UserResponseDto getUserById(Long id) {
        UserResponseDto res = repository.findById(id)
                .map(UserMapper::userToUserDtoResponse)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));

        log.info("Подготовка ответа о найденном пользователе с id {} - {}", id, res);
        return res;
    }

    @Override
    public UserResponseDto createUser(NewUserRequestDto newUserRequestDto) {
        Optional<User> existingUser = repository.findByEmail(newUserRequestDto.getEmail());

        if (existingUser.isPresent()) {
            throw new DuplicatedDataException("Email " + newUserRequestDto.getEmail() + " уже используется");
        }

        User newUser = repository.create(newUserRequestDto);
        UserResponseDto res = UserMapper.userToUserDtoResponse(newUser);

        log.info("Подготовка ответа о созданном пользователе {}", res);
        return res;
    }

    @Override
    public UserResponseDto updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto) {
        Optional<User> updatingUser = repository.findById(userId);

        if (updatingUser.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }


        if (updateUserRequestDto.hasEmail()) {
            Optional<User> existingUser = repository.findByEmail(updateUserRequestDto.getEmail());

            if (existingUser.isPresent() && !Objects.equals(existingUser.get().getId(), userId)) {
                throw new DuplicatedDataException("Email " + updateUserRequestDto.getEmail() + " уже используется");
            }
        }


        User updatedUser = repository.update(userId, updateUserRequestDto);
       UserResponseDto res = UserMapper.userToUserDtoResponse(updatedUser);

        log.info("Подготовка ответа об обновленном пользователе {}", res);
        return res;
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> deletingUser = repository.findById(userId);

        if (deletingUser.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        boolean isDeleted = repository.delete(deletingUser.get());
        log.info("Статус удаления пользователя: {}", isDeleted);

        if (!isDeleted) {
            throw new InternalServerException("Не удалось удалить пользователя. Попробуйте позднее");
        }
    }
}
