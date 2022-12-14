package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingItemInfoDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


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
    private BookingItemInfoDto nextBooking;
    private BookingItemInfoDto lastBooking;
    private List<CommentDto> comments;
}
