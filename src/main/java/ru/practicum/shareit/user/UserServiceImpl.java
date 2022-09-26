package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User get(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(id)));
    }

    @Override
    public User create(User user) throws EmailAlreadyExistsException {
        try {
            return userRepository.save(user);
        } catch (Throwable e) {
            if (e.getMessage().contains("uq_user_email"))
                throw new EmailAlreadyExistsException(EmailAlreadyExistsException.createMessage(user.getEmail()));
            else throw e;
        }
    }

    @Override
    public User update(Long id, User user) throws UserNotFoundException, EmailAlreadyExistsException {
        try {
            User userDb = userRepository.findById(id).orElseThrow(() ->
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

    @Override
    public void delete(Long id) throws UserNotFoundException {
        if (userRepository.existsById(id))
            userRepository.deleteById(id);
        else throw new UserNotFoundException(UserNotFoundException.createMessage(id));
    }
}
