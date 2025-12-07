package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewItemRequestDto {
    private final String name;
    private final String description;
    private final Boolean available;
    private final Long requestId;
}
