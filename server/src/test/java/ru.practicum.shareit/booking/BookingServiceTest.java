package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    BookingRepository bookingRepository;

    BookingService bookingService;

    private MockitoSession session;

    public static User mockUser = new User(1L, "user", "user@mail.ru");
    public static User mockOwner = new User(2L, "user2", "user2@mail.ru");

    public static User mockUser3 = new User(3L, "user3", "user3@mail.ru");

    public static ItemRequest mockItemRequest = new ItemRequest(1L, "test", mockUser,
            LocalDateTime.now().minusDays(50), new HashSet<>());

    public static Item mockItem = new Item(1L, "Дрель", "test",
            Boolean.TRUE, mockOwner, mockItemRequest, null, null, new HashSet<>());

    public static Item mockItemNotAvailable = new Item(1L, "Дрель", "test",
            Boolean.FALSE, mockOwner, mockItemRequest, null, null, new HashSet<>());

    public static Comment mockComment = new Comment(1L, "Дрель", mockItem,
            mockUser, LocalDateTime.now());

    public static Booking mockBooking = new Booking(1L, mockItem, mockUser, LocalDateTime.now().minusDays(20),
            LocalDateTime.now().minusDays(10), BookingStatus.APPROVED);

    public static Booking mockBookingNotAvailable = new Booking(1L, mockItemNotAvailable, mockUser,
            LocalDateTime.now().minusDays(20),
            LocalDateTime.now().minusDays(10), BookingStatus.APPROVED);

    public static Booking mockBookingErrorData = new Booking(1L, mockItem, mockUser,
            LocalDateTime.now().minusDays(10),
            LocalDateTime.now().minusDays(20), BookingStatus.APPROVED);


    public static Booking mockBookingUpdate = new Booking(1L, mockItem, mockUser, LocalDateTime.now().minusDays(20),
            LocalDateTime.now().minusDays(10), BookingStatus.WAITING);

    public static Booking mockBookingREJECTED = new Booking(1L, mockItem, mockUser,
            LocalDateTime.now().minusDays(20),
            LocalDateTime.now().minusDays(10), BookingStatus.REJECTED);

    @BeforeEach
    void init() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
    }

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @Test
    void testCreate() throws UserNotFoundException, ItemNotFoundException, ItemNotAvailableException,
            BookingAccessException, BookingDateNotValidException {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockOwner));

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItem));

        Mockito.when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(mockBooking);

        Assertions.assertEquals(mockBooking, bookingService.create(1L, mockBooking));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(Mockito.any(Booking.class));
    }

    @Test
    void testCreateUserNotFoundException() {

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItem));

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> bookingService.create(1L,
                mockBooking));
    }

    @Test
    void testCreateItemNotFoundException() {

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ItemNotFoundException.class, () -> bookingService.create(1L,
                mockBooking));
    }

    @Test
    void testCreateBookingAccessException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItem));

        Assertions.assertThrows(BookingAccessException.class, () -> bookingService.create(2L, mockBooking));
    }

    @Test
    void testCreateBookingDateNotValidException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockOwner));

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItem));

        Assertions.assertThrows(BookingDateNotValidException.class, () -> bookingService.create(1L,
                mockBookingErrorData));
    }

    @Test
    void testCreateItemNotAvailableException() {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItemNotAvailable));

        Assertions.assertThrows(ItemNotAvailableException.class, () -> bookingService.create(1L, mockBooking));
    }

    @Test
    void testUpdateStatus() throws UserNotFoundException,
            ItemAccessException, BookingNotFoundException, ItemChangeStatusException {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockOwner));

        Mockito.when(bookingRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockBookingUpdate));

        Mockito.when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(mockBooking);

        Assertions.assertEquals(mockBooking, bookingService.updateStatus(2L, 1L, true));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(Mockito.any(Booking.class));
    }

    @Test
    void testUpdateStatusUserNotFoundException() {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.updateStatus(2L, 1L, true));
    }

    @Test
    void testUpdateStatusBookingNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockOwner));

        Mockito.when(bookingRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(BookingNotFoundException.class,
                () -> bookingService.updateStatus(2L, 1L, true));
    }

    @Test
    void testUpdateStatusItemAccessException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(bookingRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockBookingUpdate));


        Assertions.assertThrows(ItemAccessException.class,
                () -> bookingService.updateStatus(2L, 1L, true));
    }

    @Test
    void testUpdateStatusItemChangeStatusException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockOwner));

        Mockito.when(bookingRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockBookingNotAvailable));

        Assertions.assertThrows(ItemChangeStatusException.class,
                () -> bookingService.updateStatus(2L, 1L, true));
    }

    @Test
    void testUpdateStatusItemChangeStatusExceptionREJECTED() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockOwner));

        Mockito.when(bookingRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockBookingREJECTED));

        Assertions.assertThrows(ItemChangeStatusException.class,
                () -> bookingService.updateStatus(2L, 1L, false));
    }

    @Test
    void testGetBookingById() throws UserNotFoundException, BookingNotFoundException, BookingAccessException {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(bookingRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockBooking));

        Assertions.assertEquals(mockBooking, bookingService.getBookingById(1L, 1L));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findById(Mockito.any(Long.class));
    }

    @Test
    void testGetBookingByIdBookingNotFoundException() {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(bookingRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(BookingNotFoundException.class,
                () -> bookingService.getBookingById(1L, 1L));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findById(Mockito.any(Long.class));
    }

    @Test
    void testGetBookingByIdBookingAccessException() throws UserNotFoundException, BookingNotFoundException,
            BookingAccessException {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser3));

        Mockito.when(bookingRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockBooking));

        Assertions.assertThrows(BookingAccessException.class,
                () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void testGetBookingByIdUserNotFoundException() {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingById(2L, 1L));
    }

    @Test
    void testGetAllBookingsUser() throws UserNotFoundException, InvalidParamException {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(bookingRepository.findAllByBookerIdOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsUser(1L, BookingState.ALL.toString(), 0, 2));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerIdOrderByStartDesc(Mockito.any(Long.class), Mockito.any(PageRequest.class));


        Mockito.when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsUser(1L, BookingState.FUTURE.toString(), 0, 2));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerIdAndStartAfterOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class));

        Mockito.when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsUser(1L, BookingState.PAST.toString(), 0, 2));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerIdAndEndBeforeOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class));

        Mockito.when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        Mockito.any(Long.class), Mockito.any(LocalDateTime.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsUser(1L, BookingState.CURRENT.toString(), 0, 2));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Mockito.any(Long.class), Mockito.any(LocalDateTime.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class));

        Mockito.when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(BookingStatus.class), Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsUser(1L, BookingState.WAITING.toString(), 0, 2));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByBookerIdAndStatusOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(BookingStatus.class), Mockito.any(PageRequest.class));

        Mockito.when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(BookingStatus.class), Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsUser(1L, BookingState.REJECTED.toString(), 0, 2));

        Mockito.verify(bookingRepository, Mockito.times(2))
                .findByBookerIdAndStatusOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(BookingStatus.class), Mockito.any(PageRequest.class));

        Assertions.assertThrows(InvalidParamException.class,
                () -> bookingService.getAllBookingsUser(1L, "test", 0, 2));
    }

    @Test
    void testGetAllBookingsUserUserNotFoundException() {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.getAllBookingsUser(1L, "test", 0, 2));
    }

    @Test
    void testGetAllBookingsOwner() throws UserNotFoundException, InvalidParamException {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRepository.findFirstByOwnerId(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItem));

        Mockito.when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(LocalDateTime.class),
                        Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Mockito.when(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(LocalDateTime.class),
                        Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(LocalDateTime.class),
                        Mockito.any(LocalDateTime.class),
                        Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Mockito.when(bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(BookingStatus.class),
                        Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsOwner(1L, BookingState.ALL.toString(), 0, 2));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsOwner(1L, BookingState.FUTURE.toString(), 0, 2));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsOwner(1L, BookingState.PAST.toString(), 0, 2));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsOwner(1L, BookingState.CURRENT.toString(), 0, 2));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsOwner(1L, BookingState.WAITING.toString(), 0, 2));

        Assertions.assertEquals(List.of(mockBooking),
                bookingService.getAllBookingsOwner(1L, BookingState.REJECTED.toString(), 0, 2));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByItemOwnerIdOrderByStartDesc(Mockito.any(Long.class), Mockito.any(PageRequest.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class));

        Mockito.verify(bookingRepository, Mockito.times(2))
                .findByItemOwnerIdAndStatusOrderByStartDesc(Mockito.any(Long.class),
                        Mockito.any(BookingStatus.class),
                        Mockito.any(PageRequest.class));
    }

    @Test
    void testGetAllBookingsOwnerUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.getAllBookingsOwner(1L, BookingState.WAITING.toString(), 0, 2));
    }
}