package ru.practicum.shareit.exception;

import java.time.Duration;
import java.time.LocalDateTime;

public class BookingDateNotValidException extends Exception {
    public BookingDateNotValidException(String message) {
        super(message);
    }

    public static String createMessage(LocalDateTime start, LocalDateTime end) {
        return String.format("Not valid booking date start=%1s  end=%2s ", start.toString(), end.toString());
    }
}