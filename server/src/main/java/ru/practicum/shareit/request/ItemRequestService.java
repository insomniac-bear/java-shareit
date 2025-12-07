package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItems;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto create(NewItemRequestDto req, Long userId);

    List<ItemRequestResponseWithItems> getUserRequests(Long userId);

    List<ItemRequestResponseWithItems> getAllItemRequests(Long userId);

    ItemRequestResponseWithItems getItemRequest(Long requestId, Long userId);
}
