package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {ItemMapper.class}
)
public interface ItemRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "req.description")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    ItemRequest itemRequestDtoToItemRequest(NewItemRequestDto req, User user);

    ItemRequestResponseDto itemRequestToItemRequestResponseDto(ItemRequest itemRequest);

    @Mapping(target = "id", source = "req.id")
    @Mapping(target = "description", source = "req.description")
    @Mapping(target = "created", source = "req.created")
    @Mapping(target = "items", source = "items")
    ItemRequestResponseWithItems itemRequestToItemRequestResponseWithItems(ItemRequest req, List<ItemAnswerDto> items);
}
