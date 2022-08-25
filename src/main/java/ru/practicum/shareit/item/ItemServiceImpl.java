package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemAccessException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Item create(Long ownerId, Item item) throws UserNotFoundException {
        User owner = userStorage.findUserById(ownerId);
        item.setOwner(owner);
        return itemStorage.crate(item);
    }

    @Override
    public Item findItemById(Long id) throws ItemNotFoundException {
        return itemStorage.read(id);
    }

    @Override
    public List<Item> readAllItemsOwner(Long ownerId) throws UserNotFoundException {
        userStorage.checkUserExists(ownerId);
        return itemStorage.readAllItemsOwner(ownerId);
    }

    @Override
    public Item update(Long ownerId, Long itemId, Item item) throws ItemNotFoundException, UserNotFoundException, ItemAccessException {
        User owner = userStorage.findUserById(ownerId);
        item.setOwner(owner);
        return itemStorage.update(itemId, item);
    }

    @Override
    public void delete(Long id) throws ItemNotFoundException {
        itemStorage.delete(id);
    }

    @Override
    public List<Item> searchItem(Long ownerId, String text) throws UserNotFoundException {
        userStorage.checkUserExists(ownerId);
        return itemStorage.searchItem(text);
    }
}
