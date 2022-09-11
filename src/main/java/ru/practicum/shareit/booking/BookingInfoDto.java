package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.ItemInfoDto;
import ru.practicum.shareit.user.UserInfoDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingInfoDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private UserInfoDto booker;
    private ItemInfoDto item;

}
