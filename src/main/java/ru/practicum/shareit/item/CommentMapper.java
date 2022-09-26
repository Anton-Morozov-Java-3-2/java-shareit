package ru.practicum.shareit.item;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentMapper {

    public static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(),
                comment.getCreated().format(format));
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(null, commentDto.getText(), null, null, LocalDateTime.now());
    }
}
