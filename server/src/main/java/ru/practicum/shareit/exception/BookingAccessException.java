package ru.practicum.shareit.exception;

public class BookingAccessException extends Exception {

    public BookingAccessException(String message) {
        super(message);
    }

    public static String createMessage(Long userId, Long bookingId) {
        return String.format("The user id = %d cannot get the booking  id=%d .", userId, bookingId);
    }
}
