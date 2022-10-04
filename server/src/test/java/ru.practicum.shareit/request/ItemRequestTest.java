package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;


public class ItemRequestTest {
    public static User user = new User(1L, "user", "user@mail.ru");

    public static ItemRequest itemRequest = new ItemRequest(1L, "test", user,
            LocalDateTime.now().minusDays(50), new HashSet<>());

    @Test
    public void testToString() {
        Assertions.assertEquals(itemRequest.toString(),
                String.format("ItemRequest = {id: %d, description: %s, created: %s}",
                        itemRequest.getId(),
                        itemRequest.getDescription(),
                        itemRequest.getCreated().format(ItemRequestMapper.format)));
    }

    @Test
    public void testEquals() {
        Assertions.assertEquals(itemRequest,   new ItemRequest(1L, "test", user,
                itemRequest.getCreated(), new HashSet<>()));
    }

    @Test
    public void testHashCode() {
        Assertions.assertEquals(itemRequest.hashCode(), itemRequest.hashCode());
        Assertions.assertEquals(itemRequest.hashCode(), new ItemRequest(1L, "test", user,
                itemRequest.getCreated(), new HashSet<>()).hashCode());
    }

}