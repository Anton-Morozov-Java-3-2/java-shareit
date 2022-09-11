package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User get(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(()->
                new UserNotFoundException(UserNotFoundException.createMessage(id)));
    }

    public User create(User user) throws EmailAlreadyExistsException {
        try {
            return userRepository.save(user);
        } catch (Throwable e) {
            if (e.getMessage().contains("uq_user_email"))
                throw new EmailAlreadyExistsException(EmailAlreadyExistsException.createMessage(user.getEmail()));
            else throw e;
        }
    }

    public User update(Long id, User user) throws UserNotFoundException, EmailAlreadyExistsException {
        try {
            User userDb = userRepository.findById(id).orElseThrow(()->
                    new UserNotFoundException(UserNotFoundException.createMessage(id)));

            user.setId(id);
            if (user.getEmail() == null) user.setEmail(userDb.getEmail());
            if (user.getName() == null) user.setName(userDb.getName());
            return userRepository.save(user);

        } catch (Throwable e) {
            if (e.getMessage().contains("uq_user_email"))
                throw new EmailAlreadyExistsException(EmailAlreadyExistsException.createMessage(user.getEmail()));
            else throw e;
        }
    }

    public void delete(Long id) throws UserNotFoundException {
        if (userRepository.existsById(id))
            userRepository.deleteById(id);
        else throw new UserNotFoundException(UserNotFoundException.createMessage(id));
    }
}
