package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemWithBookingDateResponseDto {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private boolean available;

    @NotNull
    private UserResponseDto user;

    private LocalDateTime lastBooking;

    private LocalDateTime nextBooking;

    private List<CommentResponseDto> comments;
}
