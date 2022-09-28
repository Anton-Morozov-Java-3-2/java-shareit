package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class UserTest {
    public static User user = new User(1L, "user", "user@mail.ru");

    @Test
    public void testToString() {
        Assertions.assertEquals(user.toString(),
                String.format("User = {id: %d, name: %s, email: %s}",
                        user.getId(),
                        user.getName(),
                        user.getEmail()));
    }

    @Test
    public void testEquals() {
        Assertions.assertEquals(user, new User(1L, "user", "user@mail.ru"));
    }

    @Test
    public void testHashCode() {
        Assertions.assertEquals(user.hashCode(), user.hashCode());
    }

    @Test
    public void testHashCodeNew() {
        Assertions.assertEquals(new User(null, "user", "user@mail.ru").hashCode(),
                new User(null, "user", "user@mail.ru").hashCode());
    }

    @Test
    public void testHashCodeNot() {
        Assertions.assertNotEquals(new User(null, null, "user@mail.ru").hashCode(),
                new User(null, "user", "user@mail.ru").hashCode());
    }

}