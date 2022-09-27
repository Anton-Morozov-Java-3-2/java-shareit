package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;


public class ItemTest {
    public static User user = new User(1L, "user", "user@mail.ru");

    public static User owner = new User(2L, "user2", "user2@mail.ru");

    public static ItemRequest itemRequest = new ItemRequest(1L, "test", user,
            LocalDateTime.now().minusDays(50), new HashSet<>());

    public static Item item = new Item(1L, "Дрель", "test",
            Boolean.TRUE, owner, itemRequest, null, null, new HashSet<>());


    @Test
    public void testToString() {
        Assertions.assertEquals(item.toString(),
                String.format("Item = {id: %d, name: %s, description: %s, isAvailable: %b}",
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getIsAvailable()));
    }

    @Test
    public void testEquals() {
        Assertions.assertEquals(item, new Item(1L, "Дрель", "test",
                Boolean.TRUE, owner, itemRequest, null, null, new HashSet<>()));
    }

    @Test
    public void testHashCode() {
        Assertions.assertEquals(item.hashCode(), item.hashCode());
    }

}