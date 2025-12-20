package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItems;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestResponseDto create(NewItemRequestDto req, Long userId) {
        User user = findUser(userId);

        ItemRequest newRequest = itemRequestMapper.itemRequestDtoToItemRequest(req, user);
        log.info("Подготовка данных запроса на вещь для сохранения: {}", newRequest);

        ItemRequest itemRequest = itemRequestRepository.save(newRequest);
        log.info("Получение данных из БД о сохранении: {}", itemRequest);

        ItemRequestResponseDto res = itemRequestMapper.itemRequestToItemRequestResponseDto(itemRequest);
        log.info("Подготовка ответа о созданном запросе вещи: {}", res);

        return res;
    }

    @Override
    public List<ItemRequestResponseWithItems> getUserRequests(Long userId) {
        User user = findUser(userId);
        List<ItemRequest> userItemRequests = itemRequestRepository.findAllByUserId(user.getId());
        log.info("Получение данных из БД: {}", userItemRequests);

        return userItemRequests.stream()
                .map(this::getItemRequestWithItems)
                .toList();
    }

    @Override
    public List<ItemRequestResponseWithItems> getAllItemRequests(Long userId) {
        findUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAll();
        log.info("Получение данных из БД: {}", itemRequests);

        return itemRequests.stream()
                .map(this::getItemRequestWithItems)
                .toList();
    }

    @Override
    public ItemRequestResponseWithItems getItemRequest(Long requestId, Long userId) {
        findUser(userId);
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Попытка получения несуществующего запроса на вещь с id {}", requestId);
                    return new NotFoundException("Запрос на вещь с id " + requestId + " не найден");
                });
        log.info("Получение запроса на вещь с id {} из БД: {}", requestId, request);

        return getItemRequestWithItems(request);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь с id {} не найден", userId);
                    return new NotFoundException("Пользователь с id " + userId + " не найден");
                });
    }

    private ItemRequestResponseWithItems getItemRequestWithItems(ItemRequest itemRequest) {
        List<ItemAnswerDto> items = itemRepository.findAllByItemRequestId(itemRequest.getId())
                .stream()
                .map(itemMapper::itemToItemAnswerDto)
                .toList();
        return itemRequestMapper.itemRequestToItemRequestResponseWithItems(itemRequest, items);
    }
}
