package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public Comment newCommentRequestDtoToComment(Item item, User author, NewCommentRequestDto comment) {
        return Comment.builder()
                .author(author)
                .text(comment.getText())
                .created(LocalDateTime.now())
                .item(item)
                .build();
    }

    public CommentResponseDto commentToCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .text(comment.getText())
                .build();
    }
}
