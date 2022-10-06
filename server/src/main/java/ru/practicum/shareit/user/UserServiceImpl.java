package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User get(Long id) throws UserNotFoundException {
        log.info("Get user id= " + id);
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(id)));
    }

    @Override
    public User create(User user) throws EmailAlreadyExistsException {
        try {
            log.info("Create " + user);
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
            log.info("Update user id= " + id  + ", new data: " + user);
            return userRepository.save(user);

        } catch (Throwable e) {
            if (e.getMessage().contains("uq_user_email"))
                throw new EmailAlreadyExistsException(EmailAlreadyExistsException.createMessage(user.getEmail()));
            else throw e;
        }
    }

    @Override
    public void delete(Long id) throws UserNotFoundException {
        if (userRepository.existsById(id)) {
            log.info("Delete user id= " + id);
            userRepository.deleteById(id);
        } else throw new UserNotFoundException(UserNotFoundException.createMessage(id));
    }
}
