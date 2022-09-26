package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.HashSet;
import java.util.List;


@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    void testSearch() {

        User user1 = userRepository.save(new User(null, "test1", "user1@mail.ru"));
        User user2 = userRepository.save(new User(null, "test2", "user2@mail.ru"));
        User user3 = userRepository.save(new User(null, "test3", "user3@mail.ru"));


        Item item1 = itemRepository.save(new Item(null, "Дрель", "test", Boolean.TRUE,
                user1,
                null,
                null,
                null,
                new HashSet<>()));
        Item item2 = itemRepository.save(new Item(null, "Дрель", "test1", Boolean.TRUE,
                user2,
                null,
                null,
                null,
                new HashSet<>()));

        Item item3 = itemRepository.save(new Item(null, "Дрель", "test 1", Boolean.TRUE,
                user3,
                null,
                null,
                null,
                new HashSet<>()));

        Assertions.assertArrayEquals(List.of(item2).toArray(),
                itemRepository.search("test1", PageRequest.of(0, 10)).toArray());
    }
}
