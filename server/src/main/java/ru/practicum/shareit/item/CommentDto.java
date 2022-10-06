package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private final String text;
    private final String authorName;
    private final String created;

}
