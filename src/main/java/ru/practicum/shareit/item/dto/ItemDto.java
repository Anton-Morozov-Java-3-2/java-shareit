package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.dto.ValidateOnUpdateUser;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotNull(groups = ValidateOnCreateItem.class)
    @NotBlank(groups = {ValidateOnCreateItem.class})
    private final String name;
    @NotNull(groups = ValidateOnCreateItem.class)
    @NotBlank(groups = {ValidateOnCreateItem.class})
    private final String description;
    @NotNull(groups = ValidateOnCreateItem.class)
    private final Boolean available;
    private Long requestId;
}
