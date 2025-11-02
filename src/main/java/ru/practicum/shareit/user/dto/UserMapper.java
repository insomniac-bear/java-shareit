package ru.practicum.shareit.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserMapper {
    public User userDtoToUser(NewUserRequestDto newUserRequestDto) {
        return User.builder()
                .name(newUserRequestDto.getName())
                .email(newUserRequestDto.getEmail())
                .build();
    }

    public UserResponseDto userToUserDtoResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User updateUserField(User user, UpdateUserRequestDto updatingUser) {
        if (updatingUser.hasName()) {
            user.setName(updatingUser.getName());
        }

        if (updatingUser.hasEmail()) {
            user.setEmail(updatingUser.getEmail());
        }

        return user;
    }
}
