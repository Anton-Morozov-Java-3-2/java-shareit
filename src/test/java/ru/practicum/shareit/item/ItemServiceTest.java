package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    ItemRequestRepository itemRequestRepository;

    ItemService itemService;

    private MockitoSession session;

    public static User mockUser = new User(1L, "user", "user@mail.ru");

    public static ItemRequest mockItemRequest = new ItemRequest(1L, "test", mockUser,
            LocalDateTime.now().minusDays(50), new HashSet<>());

    public static Item mockItem = new Item(1L, "Дрель", "test",
            Boolean.TRUE, mockUser, mockItemRequest, null, null, new HashSet<>());

    public static Comment mockComment = new Comment(1L, "Дрель", mockItem,
            mockUser, LocalDateTime.now());

    public static Booking mockBooking = new Booking(1L, mockItem, mockUser, LocalDateTime.now().minusDays(20),
            LocalDateTime.now().minusDays(10), BookingStatus.APPROVED);

    @BeforeEach
    void init() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        itemService = new ItemServiceImpl(itemRepository, userRepository,
                bookingRepository, commentRepository, itemRequestRepository);
    }

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @Test
    void testPostComment() throws UserNotFoundException, CommentNotAvailableException, ItemNotFoundException {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItem));

        Mockito.when(bookingRepository.findAllByBookerIdAndItemId(Mockito.any(Long.class),
                        Mockito.any(Long.class), Mockito.any(LocalDateTime.class),
                        Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(mockComment);

        Assertions.assertEquals(mockComment, itemService.postComment(1L,
                1L, mockComment));

        Mockito.verify(commentRepository, Mockito.times(1))
                .save(Mockito.any(Comment.class));
    }

    @Test
    void testPostCommentItemNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.postComment(1L,
                1L, mockComment));
    }

    @Test
    void testPostCommentUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> itemService.postComment(1L,
                1L, mockComment));
    }

    @Test
    void testPostCommentCommentNotAvailableException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItem));

        Mockito.when(bookingRepository.findAllByBookerIdAndItemId(Mockito.any(Long.class),
                        Mockito.any(Long.class), Mockito.any(LocalDateTime.class),
                        Mockito.any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        Assertions.assertThrows(CommentNotAvailableException.class,
                () -> itemService.postComment(1L, 1L, mockComment));
    }

    @Test
    void testCreate() throws UserNotFoundException, ItemRequestNotFoundException {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRequestRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItemRequest));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(mockItem);

        Assertions.assertEquals(mockItem, itemService.create(1L, mockItem));

        Mockito.verify(itemRepository, Mockito.times(1))
                .save(Mockito.any(Item.class));
    }

    @Test
    void testCreateUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> itemService.create(1L, mockItem));
    }

    @Test
    void testItemRequestNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRequestRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());


        Assertions.assertThrows(ItemRequestNotFoundException.class, () -> itemService.create(1L, mockItem));

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findById(Mockito.any(Long.class));
    }

    @Test
    void testFindItemById() throws UserNotFoundException, ItemNotFoundException {

        mockItem.setLastBooking(mockBooking);

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItem));

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(bookingRepository.findAllByItemIdAndStartAfterOrderByStartDesc(Mockito.any(Long.class), Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        Mockito.when(bookingRepository.findAllByItemIdAndStartBeforeOrderByStartAsc(Mockito.any(Long.class), Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));


        Assertions.assertEquals(mockItem, itemService.findItemById(1L, 1L));

        Mockito.verify(itemRepository, Mockito.times(1))
                .findById(Mockito.any(Long.class));
    }

    @Test
    void testFindItemByIdUserNotFoundException() {

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItem));

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> itemService.findItemById(1L, 1L));
    }

    @Test
    void testFindItemByIdItemNotFoundException() {

        mockItem.setLastBooking(mockBooking);

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.findItemById(1L, 1L));

        Mockito.verify(itemRepository, Mockito.times(1))
                .findById(Mockito.any(Long.class));
    }

    @Test
    void testReadAllItemsOwner() throws UserNotFoundException {

        mockItem.setLastBooking(mockBooking);

        Mockito.when(userRepository.existsById(Mockito.any(Long.class)))
                .thenReturn(true);

        Mockito.when(itemRepository.findAllByOwnerIdOrderByIdAsc(Mockito.any(Long.class),
                        Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockItem));

        Mockito.when(bookingRepository.findAllByItemIdAndStartAfterOrderByStartDesc(Mockito.any(Long.class), Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        Mockito.when(bookingRepository.findAllByItemIdAndStartBeforeOrderByStartAsc(Mockito.any(Long.class), Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockBooking));

        Assertions.assertArrayEquals(List.of(mockItem).toArray(),
                itemService.readAllItemsOwner(1L, 0, 2).toArray());

        Mockito.verify(itemRepository, Mockito.times(1))
                .findAllByOwnerIdOrderByIdAsc(Mockito.any(Long.class), Mockito.any(PageRequest.class));
    }

    @Test
    void testReadAllItemsOwnerUserNotFoundException() {

        Mockito.when(userRepository.existsById(Mockito.any(Long.class)))
                .thenReturn(false);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemService.readAllItemsOwner(1L, 0, 2));
    }

    @Test
    void testUpdate() throws UserNotFoundException, ItemAccessException, ItemNotFoundException {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItem));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(mockItem);

        Assertions.assertEquals(mockItem, itemService.update(1L, 1L, mockItem));

        Mockito.verify(itemRepository, Mockito.times(1))
                .save(Mockito.any(Item.class));
    }

    @Test
    void testUpdateUserNotFoundException() {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemService.update(1L, 1L, mockItem));
    }

    @Test
    void testUpdateItemNotFoundException() {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ItemNotFoundException.class,
                () -> itemService.update(1L, 1L, mockItem));
    }

    @Test
    void testUpdateItemAccessException() {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItem));

        Assertions.assertThrows(ItemAccessException.class,
                () -> itemService.update(2L, 1L, mockItem));
    }

    @Test
    void testDelete() {

        Mockito.when(itemRepository.existsById(Mockito.any(Long.class)))
                .thenReturn(true);

        Assertions.assertDoesNotThrow(() -> itemService.delete(1L));

        Mockito.verify(itemRepository, Mockito.times(1))
                .deleteById(Mockito.any(Long.class));
    }

    @Test
    void testDeleteItemNotFoundException() {

        Mockito.when(itemRepository.existsById(Mockito.any(Long.class)))
                .thenReturn(false);

        Assertions.assertThrows(ItemNotFoundException.class,
                () -> itemService.delete(1L));
    }


    @Test
    void testSearchItem() throws UserNotFoundException {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Mockito.when(itemRepository.search(Mockito.any(String.class), Mockito.any(PageRequest.class)))
                .thenReturn(List.of(mockItem));

        Assertions.assertArrayEquals(List.of(mockItem).toArray(),
                itemService.searchItem(1L, "test", 0, 2).toArray());

        Mockito.verify(itemRepository, Mockito.times(1))
                .search(Mockito.any(String.class), Mockito.any(PageRequest.class));
    }

    @Test
    void testSearchItemUserNotFoundException() {

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () ->  itemService.searchItem(1L, "test", 0, 2));
    }
}