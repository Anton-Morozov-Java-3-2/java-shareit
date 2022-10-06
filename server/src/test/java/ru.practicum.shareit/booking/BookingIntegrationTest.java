package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;


@Transactional
@SpringBootTest(
        properties = "db.name=shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingIntegrationTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final BookingService bookingService;

    @Test
    void testGetAllBookingsOwner() throws UserNotFoundException, ItemRequestNotFoundException, ItemNotAvailableException,
            BookingAccessException, BookingDateNotValidException, ItemNotFoundException, InvalidParamException {

        User user = userRepository.save(new User(null, "user", "user@user.ru"));
        User owner = userRepository.save(new User(null, "owner", "owner@user.ru"));
        Item item = itemService.create(owner.getId(),
                new Item(null, "Дрель", "test", Boolean.TRUE,
                        null, null, null,null, new HashSet<>()));

        Item item2 = itemService.create(owner.getId(),
                new Item(null, "Отвертка", "test", Boolean.TRUE,
                        null, null, null,null, new HashSet<>()));

        Booking booking1 = bookingService.create(user.getId(),
                BookingMapper.toBooking(
                        new BookingDto(
                                null,
                                item.getId(),
                                user.getId(),
                                LocalDateTime.now().plusDays(1),
                                LocalDateTime.now().plusDays(2), BookingStatus.APPROVED)));

        Booking booking2 = bookingService.create(user.getId(),
                BookingMapper.toBooking(
                        new BookingDto(
                                null,
                                item2.getId(),
                                user.getId(),
                                LocalDateTime.now().plusDays(3),
                                LocalDateTime.now().plusDays(6), BookingStatus.WAITING)));

        Booking booking3 = bookingService.create(user.getId(),
                BookingMapper.toBooking(
                        new BookingDto(
                                null,
                                item.getId(),
                                user.getId(),
                                LocalDateTime.now().plusDays(3),
                                LocalDateTime.now().plusDays(6), BookingStatus.CANCELED)));

        Booking booking4 = bookingService.create(user.getId(),
                BookingMapper.toBooking(
                        new BookingDto(
                                null,
                                item2.getId(),
                                user.getId(),
                                LocalDateTime.now().plusDays(7),
                                LocalDateTime.now().plusDays(8), BookingStatus.WAITING)));


        Assertions.assertArrayEquals(List.of(booking4).toArray(),
                bookingService.getAllBookingsOwner(owner.getId(), BookingState.WAITING.toString(),0, 1)
                        .toArray());

        Assertions.assertArrayEquals(List.of(booking3).toArray(),
                bookingService.getAllBookingsOwner(owner.getId(), BookingState.WAITING.toString(),1, 1)
                        .toArray());

    }
}
