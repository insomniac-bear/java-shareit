package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class ItemMapper {
    public ItemResponseDto itemToItemResponseDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public ItemWithBookingDateResponseDto itemToItemWithBookingDateResponseDto(
            Item item, LocalDateTime lastBookingDate, LocalDateTime nearestBookingDate, List<CommentResponseDto> comments
    ) {
        return ItemWithBookingDateResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .user(UserMapper.userToUserDtoResponse(item.getUser()))
                .lastBooking(lastBookingDate)
                .nextBooking(nearestBookingDate)
                .comments(comments)
                .build();
    }

    public Item newItemRequestDtoToItem(NewItemRequestDto newItemRequestDto, User user) {
        return Item.builder()
                .available(newItemRequestDto.getAvailable())
                .description(newItemRequestDto.getDescription())
                .name(newItemRequestDto.getName())
                .user(user)
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
