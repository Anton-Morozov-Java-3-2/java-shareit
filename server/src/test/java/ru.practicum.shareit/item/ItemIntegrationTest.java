package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.exception.*;
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
public class ItemIntegrationTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final BookingService bookingService;

    @Test
    void testGetItemsOwner() throws UserNotFoundException, ItemRequestNotFoundException, ItemNotAvailableException,
            BookingAccessException, BookingDateNotValidException, ItemNotFoundException, ItemAccessException,
            BookingNotFoundException, ItemChangeStatusException {
        User user = userRepository.save(new User(null, "user", "user@user.ru"));
        User owner = userRepository.save(new User(null, "owner", "owner@user.ru"));
        Item item = itemService.create(owner.getId(),
                new Item(null, "Дрель", "test", Boolean.TRUE,
                        null, null, null,null, new HashSet<>()));

        Booking booking1 = bookingService.create(user.getId(),
                BookingMapper.toBooking(
                        new BookingDto(
                                null,
                                item.getId(),
                                user.getId(),
                                LocalDateTime.now().plusDays(1),
                                LocalDateTime.now().plusDays(2), BookingStatus.WAITING)));

        Booking booking2 = bookingService.create(user.getId(),
                BookingMapper.toBooking(
                        new BookingDto(
                                null,
                                item.getId(),
                                user.getId(),
                                LocalDateTime.now().plusDays(3),
                                LocalDateTime.now().plusDays(6), BookingStatus.WAITING)));


        booking1 = bookingService.updateStatus(owner.getId(), booking2.getId(),  true);
        item = itemService.findItemById(item.getId(), owner.getId());

        Assertions.assertArrayEquals(List.of(item).toArray(),
                itemService.readAllItemsOwner(owner.getId(), 0, 2).toArray());
    }
}
