package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.ItemAccessException;
import ru.practicum.shareit.exception.ItemNotFoundException;

import java.util.List;

public interface ItemStorage {

    Item crate(Item item);

    Item read(Long id) throws ItemNotFoundException;

    List<Item> readAllItemsOwner(Long id);

    Item update(Long id, Item item) throws ItemNotFoundException, ItemAccessException;

    void delete(Long id) throws ItemNotFoundException;

    List<Item> searchItem(String text);
}
