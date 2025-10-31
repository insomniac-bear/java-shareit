package ru.practicum.shareit.item.dto;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

@NoArgsConstructor
public class ItemMapper {
    public static ItemResponseDto itemToItemResponseDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .userId(item.getUserId())
                .build();
    }

    public static Item newItemRequestDtoToItem(NewItemRequestDto newItemRequestDto) {
        return Item.builder()
                .available(newItemRequestDto.getAvailable())
                .description(newItemRequestDto.getDescription())
                .name(newItemRequestDto.getName())
                .build();
    }

    public static Item updateItemField(Item item, UpdateItemRequestDto updatingItem) {
        if (updatingItem.hasName()) {
            item.setName(updatingItem.getName());
        }

        if (updatingItem.hasDescription()) {
            item.setDescription(updatingItem.getDescription());
        }

        if (updatingItem.getAvailable() != null) {
            item.setAvailable(updatingItem.getAvailable());
        }

        return item;
    }
}
