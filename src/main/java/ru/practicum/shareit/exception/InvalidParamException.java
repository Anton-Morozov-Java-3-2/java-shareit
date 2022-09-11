package ru.practicum.shareit.exception;

public class InvalidParamException extends Exception {

    public InvalidParamException(String message) {
        super(message);
    }

    public static String createMessage(String state) {
        return String.format("Unknown state: %s", state);
    }
}
