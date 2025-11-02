package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class ItemMapper {
    public ItemResponseDto itemToItemResponseDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .userId(item.getUserId())
                .build();
    }

    public Item newItemRequestDtoToItem(NewItemRequestDto newItemRequestDto) {
        return Item.builder()
                .available(newItemRequestDto.getAvailable())
                .description(newItemRequestDto.getDescription())
                .name(newItemRequestDto.getName())
                .build();
    }

    public Item updateItemField(Item item, UpdateItemRequestDto updatingItem) {
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
