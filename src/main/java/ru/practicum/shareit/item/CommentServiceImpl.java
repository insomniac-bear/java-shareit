package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.NewCommentRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentResponseDto addComment(Long itemId, Long authorId, NewCommentRequestDto comment) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Попытка добавить комментариц несуществующей вещи с id {}", itemId);
                    return new NotFoundException("Вещь с id " + itemId + " не найдена");
                });
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> {
                    log.error("Попытка добавить комментарий несуществующим пользователем с id {}", authorId);
                    return new ForbiddenException("Доступ запрещен");
                });
        Booking booking = bookingRepository.findBookingByItem_IdAndBooker_Id(itemId, authorId)
                .orElseThrow(() -> {
                    log.error("Попытка добавить комментарий пользователем с id {}, который не брал вещь с id {}", authorId, itemId);
                    return new ForbiddenException("Доступ запрещен");
                });

        if (booking.getEnd().isAfter(LocalDateTime.now())) {
            log.error("Попытка добавить комментарий к бронированию с id {}, которое еще не окончилось", booking.getId());
            throw new ValidationException("Бронирование еще не завершилось");
        }

        Comment rawComment = CommentMapper.newCommentRequestDtoToComment(item, author, comment);
        Comment addingComment = commentRepository.save(rawComment);
        CommentResponseDto responseComment = CommentMapper.commentToCommentResponseDto(addingComment);
        log.info("Подготовка ответа о созданном комментарии {}", responseComment);

        return responseComment;
    }

    @Override
    public List<CommentResponseDto> getItemComments(Long itemId) {
        return commentRepository.findAllByItem_Id(itemId).stream()
                .map(CommentMapper::commentToCommentResponseDto)
                .toList();
    }
}
