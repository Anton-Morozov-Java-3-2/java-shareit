package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotNull
    @NotBlank
    private final String text;
    private final String authorName;
    private final String created;

}
