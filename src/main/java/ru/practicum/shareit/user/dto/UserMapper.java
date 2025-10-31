package ru.practicum.shareit.user.dto;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor
public class UserMapper {
    public static  User userDtoToUser(NewUserRequestDto newUserRequestDto) {
        return User.builder()
                .name(newUserRequestDto.getName())
                .email(newUserRequestDto.getEmail())
                .build();
    }

    public static UserDtoResponse userToUserDtoResponse(User user) {
        return UserDtoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User updateUserField(User user, UpdateUserRequestDto updatingUser) {
        if (updatingUser.hasName()) {
            user.setName(updatingUser.getName());
        }

        if (updatingUser.hasEmail()) {
            user.setEmail(updatingUser.getEmail());
        }

        return user;
    }
}
