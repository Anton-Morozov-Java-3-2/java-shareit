package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User get(Long id) throws UserNotFoundException;

    User create(User user) throws EmailAlreadyExistsException;

    User update(Long id, User user) throws UserNotFoundException, EmailAlreadyExistsException;

    void delete(Long id) throws UserNotFoundException;
}
