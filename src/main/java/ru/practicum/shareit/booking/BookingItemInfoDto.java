package ru.practicum.shareit.booking;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BookingItemInfoDto {
    Long id;
    Long bookerId;
    LocalDateTime start;
    LocalDateTime end;
}
