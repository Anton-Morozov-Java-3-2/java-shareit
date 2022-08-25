package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> getAllUsers() {
        return userStorage.readAll();
    }

    public User get(Long id) throws UserNotFoundException {
        return userStorage.findUserById(id);
    }

    public User create(User user) throws EmailAlreadyExistsException {
        return userStorage.create(user);
    }

    public User update(Long id, User user) throws UserNotFoundException, EmailAlreadyExistsException {
        return userStorage.update(id, user);
    }

    public void delete(Long id) throws UserNotFoundException {
        userStorage.delete(id);
    }
}
