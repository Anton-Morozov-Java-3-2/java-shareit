package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.CommentNotAvailableException;
import ru.practicum.shareit.exception.ItemAccessException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    public Comment postComment(Long userId, Long itemId, Comment comment) throws UserNotFoundException,
            ItemNotFoundException, CommentNotAvailableException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(userId)));
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(ItemNotFoundException.createMessage(itemId)));
        comment.setItem(item);
        comment.setAuthor(user);

        if (bookingRepository.findAllByBookerIdAndItemId(userId, itemId, LocalDateTime.now()).isEmpty())
            throw new CommentNotAvailableException(CommentNotAvailableException.createMessage(itemId));
        return commentRepository.save(comment);
    }

    @Override
    public Item create(Long ownerId, Item item) throws UserNotFoundException {
        User owner = userRepository.findById(ownerId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(ownerId)));
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Override
    public Item findItemById(Long itemId, Long userId) throws ItemNotFoundException, UserNotFoundException {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(ItemNotFoundException.createMessage(itemId)));

        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(userId)));

        if (item.getOwner().getId().equals(userId)) {

            List<Booking> nextBookingList = bookingRepository
                    .findAllByItemIdAndStartAfterOrderByStartDesc(itemId, LocalDateTime.now());
            item.setNextBooking(nextBookingList.isEmpty() ? null : nextBookingList.get(0));

            List<Booking> lastBookingList = bookingRepository
                    .findAllByItemIdAndStartBeforeOrderByStartAsc(itemId, LocalDateTime.now());
            item.setLastBooking(lastBookingList.isEmpty() ? null : lastBookingList.get(0));
        }
        return item;
    }

    @Override
    public List<Item> readAllItemsOwner(Long ownerId) throws UserNotFoundException {
        if (userRepository.existsById(ownerId)) {
            List<Item> items = itemRepository.findAllByOwnerIdOrderByIdAsc(ownerId);
            for (Item item : items) {
                List<Booking> nextBookingList = bookingRepository
                        .findAllByItemIdAndStartAfterOrderByStartDesc(item.getId(), LocalDateTime.now());
                item.setNextBooking(nextBookingList.isEmpty() ? null : nextBookingList.get(0));

                List<Booking> lastBookingList = bookingRepository
                        .findAllByItemIdAndStartBeforeOrderByStartAsc(item.getId(), LocalDateTime.now());
                item.setLastBooking(lastBookingList.isEmpty() ? null : lastBookingList.get(0));
            }
            return items;
        } else throw new UserNotFoundException(UserNotFoundException.createMessage(ownerId));
    }

    @Override
    public Item update(Long ownerId, Long itemId, Item item) throws ItemNotFoundException, UserNotFoundException,
            ItemAccessException {
        User owner = userRepository.findById(ownerId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(ownerId)));
        Item itemDb = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(ItemNotFoundException.createMessage(itemId)));

        if (itemDb.getOwner().getId().equals(ownerId)) {
            item.setId(itemId);
            item.setOwner(owner);
            if (item.getName() == null) item.setName(itemDb.getName());
            if (item.getDescription() == null) item.setDescription(itemDb.getDescription());
            if (item.getRequest() == null) item.setRequest(itemDb.getRequest());
            if (item.getIsAvailable() == null) item.setIsAvailable(itemDb.getIsAvailable());

            return itemRepository.save(item);

        } else throw new ItemAccessException(ItemAccessException.createMessage(ownerId, itemId));
    }

    @Override
    public void delete(Long id) throws ItemNotFoundException {
        if (itemRepository.existsById(id)) itemRepository.deleteById(id);
        else throw new ItemNotFoundException(ItemNotFoundException.createMessage(id));
    }

    @Override
    public List<Item> searchItem(Long ownerId, String text) throws UserNotFoundException {
        userRepository.findById(ownerId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(ownerId)));

        String searchText = text.strip().toLowerCase();
        if (searchText.isBlank()) return Collections.emptyList();
        else return itemRepository.search(searchText);
    }
}
