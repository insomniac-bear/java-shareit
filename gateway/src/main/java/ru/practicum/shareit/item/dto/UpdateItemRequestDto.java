package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class UpdateItemRequestDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasRequestId() {
        return requestId != null;
    }
}
