package ru.practicum.shareit.exception;

public class ItemRequestNotFoundException extends Exception {
    public ItemRequestNotFoundException(String message) {
        super(message);
    }

    public static String createMessage(Long id) {
        return String.format("ItemRequest with id= %d not found.", id);
    }

}