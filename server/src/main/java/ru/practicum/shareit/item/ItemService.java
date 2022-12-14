package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.*;

import java.util.List;

public interface ItemService {

    Comment postComment(Long userId, Long itemId, Comment comment) throws UserNotFoundException,
            ItemNotFoundException, CommentNotAvailableException;

    Item create(Long ownerId, Item item) throws UserNotFoundException, ItemRequestNotFoundException;

    Item findItemById(Long itemId, Long userId) throws ItemNotFoundException, UserNotFoundException;

    List<Item> readAllItemsOwner(Long ownerId, Integer from, Integer size) throws UserNotFoundException;

    Item update(Long ownerId, Long itemId, Item item) throws ItemNotFoundException, UserNotFoundException,
            ItemAccessException;

    void delete(Long id) throws ItemNotFoundException;

    List<Item> searchItem(Long ownerId, String text, Integer from, Integer size) throws UserNotFoundException;
}
