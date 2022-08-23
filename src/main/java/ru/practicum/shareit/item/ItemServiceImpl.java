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
    public Item create(Long idOwner, Item item) throws UserNotFoundException {
        User owner = userStorage.read(idOwner);
        item.setOwner(owner);
        return itemStorage.crate(item);
    }

    @Override
    public Item read(Long id) throws ItemNotFoundException {
        return itemStorage.read(id);
    }

    @Override
    public List<Item> readAllItemsOwner(Long idOwner) throws UserNotFoundException {
        userStorage.checkUserExists(idOwner);
        return itemStorage.readAllItemsOwner(idOwner);
    }

    @Override
    public Item update(Long idOwner, Long idItem, Item item) throws ItemNotFoundException, UserNotFoundException, ItemAccessException {
        User owner = userStorage.read(idOwner);
        item.setOwner(owner);
        return itemStorage.update(idItem, item);
    }

    @Override
    public void delete(Long id) throws ItemNotFoundException {
        itemStorage.delete(id);
    }

    @Override
    public List<Item> searchItem(Long idOwner, String text) throws UserNotFoundException {
        userStorage.checkUserExists(idOwner);
        return itemStorage.searchItem(text);
    }
}
