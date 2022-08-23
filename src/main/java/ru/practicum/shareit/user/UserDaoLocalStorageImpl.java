package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.*;

@Repository
@Slf4j
public class UserDaoLocalStorageImpl implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long counterId = 0L;

    private Long createId() {return ++counterId;}

    private boolean isUniqEmail(String email) {
        return !emails.contains(email);
    }

    private boolean isUserExists(Long id) {
        return users.containsKey(id);
    }

    @Override
    public void checkUserExists(Long id) throws UserNotFoundException {
        if (!isUserExists(id)) {
            String info = UserNotFoundException.createMessage(id);
            log.info(info);
            throw new UserNotFoundException(info);
        }
    }

    private void checkUserEmail(String email) throws EmailAlreadyExistsException {
        if (!isUniqEmail(email)) {
            String info = EmailAlreadyExistsException.createMessage(email);
            log.info(info);
            throw new EmailAlreadyExistsException(info);
        }
    }

    private void deleteEmail(String email) {
        emails.remove(email);
    }


    @Override
    public User create(User user) throws EmailAlreadyExistsException {
        checkUserEmail(user.getEmail());
        user.setId(createId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User read(Long id) throws UserNotFoundException {
        checkUserExists(id);
        return users.get(id);
    }

    @Override
    public User update(Long id, User user) throws UserNotFoundException, EmailAlreadyExistsException {
        checkUserExists(id);
        User dbUser = users.get(id);
        String email = user.getEmail();
        if (email!= null) {
            checkUserEmail(email);
            emails.add(email);
            deleteEmail(dbUser.getEmail());
            dbUser.setEmail(user.getEmail());
        }

        if (user.getName() !=null) {
            dbUser.setName(user.getName());
        }
        users.put(id, dbUser);
        return dbUser;
    }

    @Override
    public void delete(Long id) throws UserNotFoundException {
        checkUserExists(id);
        User user = users.get(id);
        deleteEmail(user.getEmail());
        users.remove(id);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(users.values());
    }
}
