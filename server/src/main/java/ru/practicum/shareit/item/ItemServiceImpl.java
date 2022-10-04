package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
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

    private final ItemRequestRepository itemRequestRepository;

    @Override
    public Comment postComment(Long userId, Long itemId, Comment comment) throws UserNotFoundException,
            ItemNotFoundException, CommentNotAvailableException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(userId)));
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(ItemNotFoundException.createMessage(itemId)));

        comment.setItem(item);
        comment.setAuthor(user);

        if (bookingRepository.findAllByBookerIdAndItemId(userId, itemId, LocalDateTime.now(),
                PageRequest.of(0, Integer.MAX_VALUE)).isEmpty())
            throw new CommentNotAvailableException(CommentNotAvailableException.createMessage(itemId));
        log.info("User id= " + userId + " create " + comment + " for item id= " + item);
        return commentRepository.save(comment);
    }

    @Override
    public Item create(Long ownerId, Item item) throws UserNotFoundException, ItemRequestNotFoundException {
        User owner = userRepository.findById(ownerId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(ownerId)));
        item.setOwner(owner);
        if (item.getRequest() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(item.getRequest().getId()).orElseThrow(() ->
                    new ItemRequestNotFoundException(ItemRequestNotFoundException
                            .createMessage(item.getRequest().getId())));
            item.setRequest(itemRequest);
        }
        log.info("User id= " + owner + " create " + item);
        return itemRepository.save(item);
    }

    @Override
    public Item findItemById(Long itemId, Long userId) throws ItemNotFoundException, UserNotFoundException {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(ItemNotFoundException.createMessage(itemId)));

        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(userId)));

        log.info("User id= " + userId + " find item by id= " + itemId);
        if (item.getOwner().getId().equals(userId)) {

            List<Booking> nextBookingList = bookingRepository
                    .findAllByItemIdAndStartAfterOrderByStartDesc(itemId, LocalDateTime.now(),
                            PageRequest.of(0, Integer.MAX_VALUE));
            item.setNextBooking(nextBookingList.isEmpty() ? null : nextBookingList.get(0));

            List<Booking> lastBookingList = bookingRepository
                    .findAllByItemIdAndStartBeforeOrderByStartAsc(itemId, LocalDateTime.now(),
                            PageRequest.of(0, Integer.MAX_VALUE));
            item.setLastBooking(lastBookingList.isEmpty() ? null : lastBookingList.get(0));
        }
        return item;
    }

    @Override
    public List<Item> readAllItemsOwner(Long ownerId, Integer from, Integer size) throws UserNotFoundException {
        if (userRepository.existsById(ownerId)) {
            List<Item> items;

            PageRequest pageRequest = PageRequest.of(from == null ? 0 : from / size,
                    size == null ? Integer.MAX_VALUE : size);

            items = itemRepository.findAllByOwnerIdOrderByIdAsc(ownerId, pageRequest);

            log.info("Owner id= " + ownerId + " find all his items by");
            for (Item item : items) {
                List<Booking> nextBookingList = bookingRepository
                        .findAllByItemIdAndStartAfterOrderByStartDesc(item.getId(), LocalDateTime.now(),
                                PageRequest.of(0, Integer.MAX_VALUE));
                item.setNextBooking(nextBookingList.isEmpty() ? null : nextBookingList.get(0));

                List<Booking> lastBookingList = bookingRepository
                        .findAllByItemIdAndStartBeforeOrderByStartAsc(item.getId(), LocalDateTime.now(),
                                PageRequest.of(0, Integer.MAX_VALUE));
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

            log.info("Owner id= " + ownerId + " update item id= " + itemId + " " + item);
            return itemRepository.save(item);

        } else throw new ItemAccessException(ItemAccessException.createMessage(ownerId, itemId));
    }

    @Override
    public void delete(Long id) throws ItemNotFoundException {
        log.info("Delete item id= " + id);
        if (itemRepository.existsById(id)) itemRepository.deleteById(id);
        else throw new ItemNotFoundException(ItemNotFoundException.createMessage(id));
    }

    @Override
    public List<Item> searchItem(Long ownerId, String text, Integer from, Integer size) throws UserNotFoundException {
        userRepository.findById(ownerId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(ownerId)));

        String searchText = text.strip().toLowerCase();
        if (searchText.isBlank()) return Collections.emptyList();

        PageRequest pageRequest = PageRequest.of(from == null ? 0 : from / size,
                size == null ? Integer.MAX_VALUE : size);

        log.info("User id= " + ownerId + " search items by text= " + text);
        return itemRepository.search(searchText, pageRequest);
    }
}
