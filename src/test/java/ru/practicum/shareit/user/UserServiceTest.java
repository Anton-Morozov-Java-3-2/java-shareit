package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    UserService userService;

    private MockitoSession session;

    public static User mockUser = new User(1L, "user", "user@mail.ru");


    @BeforeEach
    void init() {
        session = Mockito.mockitoSession().initMocks(this).startMocking();
        userService = new UserServiceImpl(userRepository);
    }

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @Test
    void testCreate() throws EmailAlreadyExistsException {
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(mockUser);

        Assertions.assertEquals(mockUser, userService.create(mockUser));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));
    }


    @Test
    void testCreateEmailAlreadyExistsException()  {
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new RuntimeException("uq_user_email"));

        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> userService.create(mockUser));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));
    }

    @Test
    void testCreateEmailAlreadyExistsExceptionThrow()  {
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new RuntimeException("uq_user_email"));

        Assertions.assertThrows(Exception.class, () -> userService.create(mockUser));

    }

    @Test
    void testGetAllUsers() {
        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(mockUser));

        Assertions.assertArrayEquals(List.of(mockUser).toArray(),
                                    userService.getAllUsers().toArray());

        Mockito.verify(userRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    void testGet() throws UserNotFoundException {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Assertions.assertEquals(mockUser, userService.get(1L));

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.any(Long.class));
    }

    @Test
    void testUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.get(120L));

        Mockito.verify(userRepository, Mockito.times(1))
                .findById(Mockito.any(Long.class));
    }

    @Test
    void testUpdate() throws EmailAlreadyExistsException, UserNotFoundException {
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(mockUser);

        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));

        Assertions.assertEquals(mockUser, userService.update(1L, mockUser));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));
    }

    @Test
    void testUpdateEmailAlreadyExistsException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new RuntimeException("uq_user_email"));
        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> userService.update(1L, mockUser));
    }

    @Test
    void testUpdateEmailAlreadyExistsExceptionThrow() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.ofNullable(mockUser));
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new RuntimeException(""));
        Assertions.assertThrows(Exception.class, () -> userService.update(1L, mockUser));
    }


    @Test
    void testUpdateUserNotFoundException() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.update(1L, mockUser));
    }

    @Test
    void testDelete() {
        Mockito.when(userRepository.existsById(Mockito.any(Long.class)))
                .thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.delete(1L));


        Mockito.verify(userRepository, Mockito.times(1))
                .deleteById(Mockito.any(Long.class));
    }

    @Test
    void testDeleteUserNotFoundException() {
        Mockito.when(userRepository.existsById(Mockito.any(Long.class)))
                .thenReturn(false);

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.delete(1L));
    }
}