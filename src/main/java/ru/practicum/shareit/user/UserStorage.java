package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;

public interface UserStorage {
    User create(User user) throws EmailAlreadyExistsException;
    User read(Long id) throws UserNotFoundException;

    List<User> readAll();
    User update(Long id, User user) throws UserNotFoundException, EmailAlreadyExistsException;
    void delete(Long id) throws UserNotFoundException;

    void checkUserExists(Long id) throws UserNotFoundException;
}
