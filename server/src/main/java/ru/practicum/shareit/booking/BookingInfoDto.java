package ru.practicum.shareit.booking;

import lombok.Value;
import ru.practicum.shareit.item.ItemInfoDto;
import ru.practicum.shareit.user.UserInfoDto;

@Value
public class BookingInfoDto {
    Long id;
    String start;
    String end;
    BookingStatus status;
    UserInfoDto booker;
    ItemInfoDto item;
}
