package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ItemAccessException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(Long ownerId, Item item) throws UserNotFoundException;

    Item findItemById(Long id) throws ItemNotFoundException;

    List<Item> readAllItemsOwner(Long ownerId) throws UserNotFoundException;

    Item update(Long ownerId, Long itemId, Item item) throws ItemNotFoundException, UserNotFoundException,
            ItemAccessException;

    void delete(Long id) throws ItemNotFoundException;

    List<Item> searchItem(Long ownerId, String text) throws UserNotFoundException;
}
