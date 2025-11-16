package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.NewCommentRequestDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(Long itemId, Long authorId, NewCommentRequestDto comment);

    List<CommentResponseDto> getItemComments(Long itemId);
}
