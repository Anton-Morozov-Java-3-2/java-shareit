package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemInfoDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserInfoDto;

import java.time.format.DateTimeFormatter;

public class BookingMapper {

    public static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus());
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                new Item(bookingDto.getItemId(),null,null, null, null, null,
                        null, null, null),
                new User(bookingDto.getBookerId(), null, null),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getStatus());
    }

    public static BookingInfoDto toBookingInfoDto(Booking booking) {
        return new BookingInfoDto(booking.getId(), booking.getStart().format(format), booking.getEnd().format(format),
                booking.getStatus(),
                new UserInfoDto(booking.getBooker().getId()),
                new ItemInfoDto(booking.getItem().getId(), booking.getItem().getName(),
                        null, null, null));
    }

    public static BookingItemInfoDto toBookingItemInfoDto(Booking booking) {
        return new BookingItemInfoDto(booking.getId(), booking.getBooker().getId(),
                booking.getStart().format(format), booking.getEnd().format(format));
    }
}
