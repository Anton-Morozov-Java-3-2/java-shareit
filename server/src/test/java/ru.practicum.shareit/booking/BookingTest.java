package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;


public class BookingTest {
    public static User user = new User(1L, "user", "user@mail.ru");

    public static User owner = new User(2L, "user2", "user2@mail.ru");

    public static ItemRequest itemRequest = new ItemRequest(1L, "test", user,
            LocalDateTime.now().minusDays(50), new HashSet<>());

    public static Item mockItem = new Item(1L, "Дрель", "test",
            Boolean.TRUE, owner, itemRequest, null, null, new HashSet<>());

    public static Booking booking = new Booking(1L, mockItem, user, LocalDateTime.now().minusDays(20),
            LocalDateTime.now().minusDays(10), BookingStatus.APPROVED);

    @Test
    public void testToString() {
        Assertions.assertEquals(booking.toString(),
                String.format("Booking = {id: %d, start: %s, end: %s, status: %s}",
                        booking.getId(),
                        booking.getStart().format(BookingMapper.format),
                        booking.getEnd().format(BookingMapper.format),
                        booking.getStatus().toString()));
    }

    @Test
    public void testEquals() {
        Assertions.assertEquals(booking, new Booking(1L, mockItem, user, booking.getStart(),
                booking.getEnd(), BookingStatus.APPROVED));
    }

    @Test
    public void testHashCode() {
        Assertions.assertEquals(booking.hashCode(), booking.hashCode());
    }

}