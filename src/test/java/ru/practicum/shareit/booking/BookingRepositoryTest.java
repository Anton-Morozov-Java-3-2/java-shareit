package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;


@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAllByBookerIdAndItemId() {

        User user1 = userRepository.save(new User(null, "user1", "user1@mail.ru"));
        User user2 = userRepository.save(new User(null, "user2", "user2@mail.ru"));
        User user3 = userRepository.save(new User(null, "user3", "user3@mail.ru"));

        Item item1 = itemRepository.save(new Item(null, "Дрель", "Test", Boolean.TRUE,
                user1,null,null, null, new HashSet<>()));

        Item item2 = itemRepository.save(new Item(null, "Дрель", "Test", Boolean.FALSE,
                user2, null, null, null, new HashSet<>()));

        Item item3 = itemRepository.save(new Item(null, "Дрель", "Test", Boolean.TRUE,
                user2, null, null, null, new HashSet<>()));

        LocalDateTime srart1 = LocalDateTime.now().minusDays(2);
        LocalDateTime end1 = srart1.minusDays(1);

        LocalDateTime srart2 = end1.plusDays(2);
        LocalDateTime end2 = srart2.plusDays(5);

        LocalDateTime srart3 = end2;
        LocalDateTime end3 = srart3.plusDays(6);

        LocalDateTime srart4 = end3;
        LocalDateTime end4 = srart4.plusDays(7);

        LocalDateTime srart5 = end4;
        LocalDateTime end5 = srart5.plusDays(8);

        LocalDateTime srart6 = end5;
        LocalDateTime end6 = srart6.plusDays(8);

        LocalDateTime srart7 = LocalDateTime.now().minusDays(7);
        LocalDateTime end7 = LocalDateTime.now().minusDays(6);

        Booking booking1 = bookingRepository.save(new Booking(null, item1, user2, srart1,
                end1, BookingStatus.APPROVED));

        Booking booking2 = bookingRepository.save(new Booking(null, item1, user2, srart2,
                end2, BookingStatus.WAITING));

        Booking booking3 = bookingRepository.save(new Booking(null, item2, user2, srart3,
                end3, BookingStatus.APPROVED));

        Booking booking4 = bookingRepository.save(new Booking(null, item1, user3, srart4,
                end4, BookingStatus.APPROVED));

        Booking booking5 = bookingRepository.save(new Booking(null, item1, user2, srart5,
                end6, BookingStatus.REJECTED));

        Booking booking6 = bookingRepository.save(new Booking(null, item1, user2, srart7,
                end7, BookingStatus.APPROVED));

        Assertions.assertArrayEquals(List.of(booking6).toArray(),
                bookingRepository.findAllByBookerIdAndItemId(2L, 1L,
                        LocalDateTime.now(),
                        PageRequest.of(1,1)).toArray());
    }
}
