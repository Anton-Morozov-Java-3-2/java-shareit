package ru.practicum.shareit.exception;

public class ItemAccessException extends Exception {

    public ItemAccessException(String message) {
        super(message);
    }

    public static String createMessage(Long userId, Long itemId) {
        return String.format("The user id = %d cannot access editing the item id=%d .", userId, itemId);
    }
}
