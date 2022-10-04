package ru.practicum.shareit.booking.dto;

import lombok.Value;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.user.dto.UserInfoDto;

@Value
public class BookingInfoDto {
    Long id;
    String start;
    String end;
    BookingStatus status;
    UserInfoDto booker;
    ItemInfoDto item;
}
