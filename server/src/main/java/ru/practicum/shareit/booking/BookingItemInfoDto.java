package ru.practicum.shareit.booking;

import lombok.Value;

@Value
public class BookingItemInfoDto {
    Long id;
    Long bookerId;
    String start;
    String end;
}
