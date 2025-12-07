package ru.practicum.shareit.item.dto;

import org.mapstruct.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "available", source = "item.available")
    ItemResponseDto itemToItemResponseDto(Item item);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "available", source = "item.available")
    @Mapping(target = "lastBooking", source = "lastBookingDate")
    @Mapping(target = "nextBooking", source = "nearestBookingDate")
    @Mapping(target = "comments", source = "comments")
    ItemWithBookingDateResponseDto itemToItemWithBookingDateResponseDto(
            Item item,
            LocalDateTime lastBookingDate,
            LocalDateTime nearestBookingDate,
            List<CommentResponseDto> comments
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "newItemRequestDto.name")
    @Mapping(target = "description", source = "newItemRequestDto.description")
    @Mapping(target = "available", source = "newItemRequestDto.available")
    @Mapping(target = "itemRequest", source = "request")
    @Mapping(target = "user", source = "user")
    Item newItemRequestDtoToItem(NewItemRequestDto newItemRequestDto, ItemRequest request, User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", source = "updatingItem.name")
    @Mapping(target = "description", source = "updatingItem.description")
    @Mapping(target = "available", source = "updatingItem.available")
    Item updateItemField(@MappingTarget Item item, UpdateItemRequestDto updatingItem);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "userId", source = "item.user.id")
    @Mapping(target = "name", source = "item.name")
    ItemAnswerDto itemToItemAnswerDto(Item item);
}