package ru.practicum.shareit.request;

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
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {

    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    UserRepository userRepository;
    ItemRequestService itemRequestService;

    private MockitoSession session;

    public static User mockUser = new User(1L, "user", "user@mail.ru");

    public static ItemRequest mockItemRequest = new ItemRequest(1L, "test",
            mockUser, LocalDateTime.now().withSecond(0).withNano(0), new HashSet<>());
    public static ItemRequestDto mockItemRequestDto = new ItemRequestDto(1L, "test",
            mockItemRequest.getCreated().format(ItemRequestMapper.format));

    @BeforeEach
    void init() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository);
    }

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @Test
    void testCreate() throws UserNotFoundException {
        Mockito.when(itemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(mockItemRequest);

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(mockUser));

        Assertions.assertEquals(mockItemRequestDto, itemRequestService.create(1L, mockItemRequestDto));

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .save(Mockito.any(ItemRequest.class));
    }

    @Test
    void testCreateUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemRequestService.create(1L, mockItemRequestDto));
    }

    @Test
    void testFindAllByRequestorIdUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemRequestService.findAllByRequestorId(1L));
    }

    @Test
    void testFindAllUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemRequestService.findAll(1L, 0, 2));
    }

    @Test
    void testFindByIdUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemRequestService.findById(1L, 1L));
    }

    @Test
    void testFindAllByRequestorId() throws UserNotFoundException {
        Mockito.when(itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(Mockito.any(Long.class)))
                .thenReturn(List.of(mockItemRequest));

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(mockUser));

        Assertions.assertArrayEquals(List.of(mockItemRequestDto).toArray(),
                                    itemRequestService.findAllByRequestorId(1L).toArray());

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findAllByRequestorIdOrderByCreatedDesc(Mockito.any(Long.class));
    }

    @Test
    void testFindAll() throws UserNotFoundException {
        Mockito.when(itemRequestRepository.findAllByRequestorIdNot(Mockito.any(Long.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(mockItemRequest));

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(mockUser));

        Assertions.assertArrayEquals(List.of(mockItemRequestDto).toArray(),
                itemRequestService.findAll(1L, 0, 2).toArray());

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findAllByRequestorIdNot(Mockito.any(Long.class), Mockito.any(PageRequest.class));
    }

    @Test
    void testFindById() throws UserNotFoundException, ItemRequestNotFoundException, ItemNotFoundException {
        Mockito.when(itemRequestRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockItemRequest));

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(mockUser));

        Assertions.assertEquals(mockItemRequestDto,
                itemRequestService.findById(1L, 1L));

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findById(Mockito.any(Long.class));
    }
}