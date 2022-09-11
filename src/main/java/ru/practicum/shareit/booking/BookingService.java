package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.*;

import java.util.List;

@Service
public interface BookingService {
    Booking create(Long bookerId, Booking booking) throws ItemNotFoundException, UserNotFoundException,
            BookingDateNotValidException, ItemNotAvailableException, BookingAccessException;

    Booking updateStatus(Long ownerId, Long bookingId, Boolean approved) throws UserNotFoundException,
            BookingNotFoundException, ItemAccessException, ItemChangeStatusException;

    Booking getBookingById(Long requesterId, Long bookingId) throws UserNotFoundException,
            BookingNotFoundException, BookingAccessException;

    List<Booking> getAllBookingsUser(Long userId, String state) throws UserNotFoundException, InvalidParamException;

    List<Booking> getAllBookingsOwner(Long userId, String state) throws UserNotFoundException, InvalidParamException;
}
