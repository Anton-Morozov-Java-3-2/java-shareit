package ru.practicum.shareit.exception;

public class BookingNotFoundException extends Exception {
    public BookingNotFoundException(String message) {
        super(message);
    }

    public static String createMessage(Long id) {
        return String.format("Booking id = %d not found ", id);
    }
}