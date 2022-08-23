package ru.practicum.shareit.exception;

public class ItemNotFoundException extends Exception{
    public ItemNotFoundException(String message) {
        super(message);
    }
    public static String createMessage(Long id) {
        return String.format("Item with id= %d not found.", id);
    }

}