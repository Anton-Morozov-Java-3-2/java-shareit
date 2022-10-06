package ru.practicum.shareit.exception;

public class CommentNotAvailableException extends Exception {
    public CommentNotAvailableException(String message) {
        super(message);
    }

    public static String createMessage(Long id) {
        return String.format("Post comment to item id=%d not available", id);
    }
}