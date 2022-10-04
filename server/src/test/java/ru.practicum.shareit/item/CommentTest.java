package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;


public class CommentTest {
    public static User user = new User(1L, "user", "user@mail.ru");

    public static User owner = new User(2L, "user2", "user2@mail.ru");

    public static ItemRequest itemRequest = new ItemRequest(1L, "test", user,
            LocalDateTime.now().minusDays(50), new HashSet<>());

    public static Item item = new Item(1L, "Дрель", "test",
            Boolean.TRUE, owner, itemRequest, null, null, new HashSet<>());

    public static Comment comment = new Comment(1L, "Дрель", item,
            user, LocalDateTime.now());

    @Test
    public void testToString() {
        Assertions.assertEquals(comment.toString(),
                String.format("Comment = {id: %d, text: %s, created: %s}",
                        comment.getId(),
                        comment.getText(),
                        comment.getCreated().format(CommentMapper.format)));
    }

    @Test
    public void testEquals() {
        Assertions.assertEquals(comment,  new Comment(1L, "Дрель", item,
                user, comment.getCreated()));
    }

    @Test
    public void testHashCode() {
        Assertions.assertEquals(comment.hashCode(), comment.hashCode());
    }

}