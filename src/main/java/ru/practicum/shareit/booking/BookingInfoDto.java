package ru.practicum.shareit.booking;

import lombok.Value;
import ru.practicum.shareit.item.ItemInfoDto;
import ru.practicum.shareit.user.UserInfoDto;

import java.time.LocalDateTime;

@Value
public class BookingInfoDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;
    UserInfoDto booker;
    ItemInfoDto item;

}
