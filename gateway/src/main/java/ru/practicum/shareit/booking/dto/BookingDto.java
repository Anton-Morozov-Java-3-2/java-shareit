package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;
    @Future(groups = ValidateOnCreateBooking.class)
    @NotNull(groups = ValidateOnCreateBooking.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;
    private BookingStatus status;
}
