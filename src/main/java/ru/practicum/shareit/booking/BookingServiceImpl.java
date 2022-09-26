package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements  BookingService {

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public Booking create(Long bookerId, Booking booking) throws ItemNotFoundException, UserNotFoundException,
            BookingDateNotValidException, ItemNotAvailableException, BookingAccessException {

        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(() ->
                new ItemNotFoundException(ItemNotFoundException.createMessage(booking.getItem().getId())));

        User booker = userRepository.findById(bookerId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(bookerId)));

        if (item.getOwner().getId().equals(bookerId))
            throw new BookingAccessException(BookingAccessException.createMessage(bookerId, item.getId()));

        booking.setItem(item);
        booking.setBooker(booker);

        if (booking.getStart().isAfter(booking.getEnd()))
            throw new BookingDateNotValidException(BookingDateNotValidException
                    .createMessage(booking.getStart(), booking.getEnd()));

        if (item.getIsAvailable()) {
            booking.setStatus(BookingStatus.WAITING);
            return bookingRepository.save(booking);
        } else {
            throw new ItemNotAvailableException(ItemNotAvailableException.createMessage(item.getId()));
        }
    }

    @Override
    public Booking updateStatus(Long ownerId, Long bookingId, Boolean approved) throws UserNotFoundException,
            BookingNotFoundException, ItemAccessException, ItemChangeStatusException {

        User owner = userRepository.findById(ownerId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(ownerId)));

        Booking bookingDb = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException(BookingNotFoundException.createMessage(bookingId)));

        Item item = bookingDb.getItem();

        if (owner.getId().equals(item.getOwner().getId())) {

            if (approved && bookingDb.getStatus().equals(BookingStatus.APPROVED))
                throw new ItemChangeStatusException(ItemChangeStatusException.createMessage(item.getId(),
                        BookingStatus.APPROVED.toString()));

            if (!approved && bookingDb.getStatus().equals(BookingStatus.REJECTED))
                throw new ItemChangeStatusException(ItemChangeStatusException.createMessage(item.getId(),
                        BookingStatus.REJECTED.toString()));

            bookingDb.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
            return bookingRepository.save(bookingDb);
        } else {
            throw new ItemAccessException(ItemAccessException.createMessage(ownerId, item.getId()));
        }
    }

    @Override
    public Booking getBookingById(Long requesterId, Long bookingId) throws UserNotFoundException, BookingNotFoundException,
            BookingAccessException {
        User requester = userRepository.findById(requesterId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(requesterId)));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException(BookingNotFoundException.createMessage(bookingId)));

        if (requester.getId().equals(booking.getItem().getOwner().getId()) ||
                requester.getId().equals(booking.getBooker().getId())) {
            return booking;
        } else {
            throw new BookingAccessException(BookingAccessException.createMessage(requesterId, bookingId));
        }
    }

    @Override
    public List<Booking> getAllBookingsUser(Long userId, String state, Integer from, Integer size)
            throws UserNotFoundException, InvalidParamException {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(userId)));

        BookingState choice = BookingState.getBookingState(state);
        PageRequest pageRequest = PageRequest.of(from == null ? 0 : from / size, size == null ? Integer.MAX_VALUE : size);

        switch (choice) {
            case ALL:
                return bookingRepository.findAllByBooker_IdOrderByStartDesc(userId, pageRequest);
            case FUTURE:
                return bookingRepository.findAllByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                        pageRequest);
            case PAST:
                return bookingRepository.findAllByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                        pageRequest);
            case CURRENT:
                return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(),
                        LocalDateTime.now(), pageRequest);
            case WAITING:
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING,
                        pageRequest);

            case REJECTED:
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED,
                        pageRequest);
            default:
                throw new InvalidParamException(InvalidParamException.createMessage(state));
        }
    }

    @Override
    public List<Booking> getAllBookingsOwner(Long userId, String state, Integer from, Integer size)
            throws UserNotFoundException, InvalidParamException {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(userId)));

        itemRepository.findFirstByOwnerId(userId)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.createMessage(userId)));

        BookingState choice = BookingState.getBookingState(state);
        PageRequest pageRequest = PageRequest.of(from == null ? 0 : from / size, size == null ? Integer.MAX_VALUE : size);

        switch (choice) {
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId, pageRequest);
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(),
                        pageRequest);
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                        pageRequest);
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(),
                        LocalDateTime.now(), pageRequest);
            case WAITING:
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING,
                        pageRequest);
            case REJECTED:
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED,
                        pageRequest);
            default:
                throw new InvalidParamException(InvalidParamException.createMessage(state));
        }
    }
}
