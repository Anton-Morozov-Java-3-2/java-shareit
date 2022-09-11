package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aspectj.lang.annotation.After;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ValidateOnCreateItem;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private Long id;
    @NotNull(groups = {ValidateOnCreateBooking.class, ValidateOnUpdateBooking.class})
    private Long itemId;
    private Long bookerId;
    @NotNull(groups = ValidateOnCreateBooking.class)
    @FutureOrPresent(groups = ValidateOnCreateBooking.class)
    private LocalDateTime start;
    @Future(groups = ValidateOnCreateBooking.class)
    @NotNull(groups = ValidateOnCreateBooking.class)
    private LocalDateTime end;
    private BookingStatus status;
}
