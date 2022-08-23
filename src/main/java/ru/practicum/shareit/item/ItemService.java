package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ItemAccessException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Long idOwner, Item item) throws UserNotFoundException;
    Item read(Long id) throws ItemNotFoundException;
    List<Item> readAllItemsOwner(Long Owner) throws UserNotFoundException;
    Item update(Long idOwner, Long idItem, Item item) throws ItemNotFoundException, UserNotFoundException, ItemAccessException;
    void delete(Long id) throws ItemNotFoundException;
    List<Item> searchItem(Long idOwner, String text) throws UserNotFoundException;
}
