package ru.practicum.shareit.exception;

import java.time.LocalDateTime;

public class ItemNotAvailableException extends Exception {
    public ItemNotAvailableException(String message) {
        super(message);
    }

    public static String createMessage(Long id) {
        return String.format("Item id=%d not available", id);
    }
}