package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingItemInfoDto {
    private Long id;
    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}
