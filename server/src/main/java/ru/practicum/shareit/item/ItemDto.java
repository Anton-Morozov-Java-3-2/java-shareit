package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingItemInfoDto;

import java.util.List;


@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private final String name;
    private final String description;
    private final Boolean available;
    private Long requestId;
    private BookingItemInfoDto nextBooking;
    private BookingItemInfoDto lastBooking;
    private List<CommentDto> comments;
}
