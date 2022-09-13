package ru.practicum.shareit.exception;

public class ItemChangeStatusException extends Exception {

    public ItemChangeStatusException(String message) {
        super(message);
    }

    public static String createMessage(Long itemId, String status) {
        return String.format("Item id= %d already exist status %s", itemId, status);
    }
}
